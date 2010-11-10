package edu.zao.fire.views.browser;

import java.io.File;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.zao.fire.Renamer;
import edu.zao.fire.Renamer.EventType;
import edu.zao.fire.RenamerRule;
import edu.zao.fire.views.browser.urlassist.URLContentProposalProvider;

public class BrowserView extends ViewPart {

	private final Renamer renamer = new Renamer();

	private final BrowserURLHistory urlHistory = new BrowserURLHistory();

	private Button historyBackButton;

	private Button historyForwardButton;

	private Button browserUpLevelButton;

	private TableViewer browserTableViewer;

	private Text currentURLText;

	private Button browseButton;

	private Button applyButton;

	private Button undoButton;

	private Button redoButton;

	/**
	 * Constructor. Initializes the internal renamer instance at the user's home
	 * directory. This function should not be called by the client- the class is
	 * initialized through Eclipse RCP and the perspective associated with FiRE.
	 */
	public BrowserView() {
		String userHomePath = System.getProperty("user.home");
		File userHome = new File(userHomePath);
		renamer.setCurrentDirectory(userHome);
	}

	/**
	 * Initialize all the User Interface components within this BrowserView.
	 * This function is called by Eclipse RCP at initialization time.
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		// create the top toolbar area
		Composite toolbarTopArea = new Composite(parent, SWT.NONE);
		toolbarTopArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		GridLayout toolbarTopAreaLayout = new GridLayout(5, false);
		toolbarTopAreaLayout.marginHeight = 0;
		toolbarTopAreaLayout.marginWidth = 0;
		toolbarTopArea.setLayout(toolbarTopAreaLayout);

		// create the "Go Back" button
		historyBackButton = new Button(toolbarTopArea, SWT.PUSH);
		historyBackButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_BACK));

		// create the "Go Forward" button
		historyForwardButton = new Button(toolbarTopArea, SWT.PUSH);
		historyForwardButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD));

		// create the "Up One Level" button
		browserUpLevelButton = new Button(toolbarTopArea, SWT.PUSH);
		browserUpLevelButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_UP));

		currentURLText = new Text(toolbarTopArea, SWT.SINGLE | SWT.BORDER);
		currentURLText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		KeyStroke keyStroke = null;
		try {
			keyStroke = KeyStroke.getInstance("Ctrl+Space");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		char[] autoActivationCharacters = new char[(126 - 32) + 1];
		for (int i = 32; i <= 126; i++)
			autoActivationCharacters[i - 32] = (char) i;

		ContentProposalAdapter urlProposalAdapter = new ContentProposalAdapter(currentURLText, new TextContentAdapter(),
				new URLContentProposalProvider(urlHistory), keyStroke, autoActivationCharacters);

		// create the Browse button
		browseButton = new Button(toolbarTopArea, SWT.PUSH);
		browseButton.setText("Browse...");
		browseButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		// create the browser table viewer
		createBrowserTableViewer(parent);

		// create the area for the bottom bar
		Composite bottomBarArea = new Composite(parent, SWT.NONE);
		GridLayout bottomBarLayout = new GridLayout(4, false);
		bottomBarLayout.marginHeight = 0;
		bottomBarLayout.marginWidth = 0;
		bottomBarArea.setLayout(bottomBarLayout);

		bottomBarArea.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

		// add undo/redo buttons
		undoButton = new Button(bottomBarArea, SWT.PUSH);
		undoButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_UNDO));

		redoButton = new Button(bottomBarArea, SWT.PUSH);
		redoButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_REDO));

		applyButton = new Button(bottomBarArea, SWT.PUSH);
		applyButton.setText("Apply Changes");
		applyButton.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false));

		Button limitDisplayCheck = new Button(bottomBarArea, SWT.CHECK);
		limitDisplayCheck.setText("Only display changing items");
		limitDisplayCheck
				.setToolTipText("If checked, the browser will only show items whose names\n would be changed by applying the current renaming rule.");

		addListeners();

		new RenamerUIAdapter(renamer).installListeners();

		urlHistory.visitLocation(renamer.getCurrentDirectory().getAbsolutePath());
		sendBrowserToLocation(renamer.getCurrentDirectory());
	}

	/**
	 * Create the browser area and the listeners associated with it.
	 * 
	 * @param parent
	 *            The parent Composite that the browser area will be created in.
	 */
	private void createBrowserTableViewer(Composite parent) {
		// create the table viewer for the browser
		browserTableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		browserTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browserTableViewer.setContentProvider(new BrowserTableContentProvider());

		final BrowserTableItemSorter tableItemSorter = new BrowserTableItemSorter();
		browserTableViewer.setSorter(tableItemSorter);

		String[] titles = { "Original Name", "New Name" };
		int[] bounds = { 180, 180 };

		for (int index = 0; index < titles.length; index++) {
			final TableViewerColumn column = new TableViewerColumn(browserTableViewer, SWT.LEFT);
			column.getColumn().setText(titles[index]);
			column.getColumn().setWidth(bounds[index]);
			column.getColumn().setResizable(true);
		}
		browserTableViewer.setLabelProvider(new BrowserTableLabelProvider(renamer));

		final Table table = browserTableViewer.getTable();
		final TableColumn sortColumn = table.getColumn(0);
		sortColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSorter.setColumn(0);
				int dir = table.getSortDirection();
				dir = (dir == SWT.UP) ? SWT.DOWN : SWT.UP;
				table.setSortDirection(dir);
				table.setSortColumn(sortColumn);
				browserTableViewer.refresh();
			}
		});

		browserTableViewer.getTable().setHeaderVisible(true);

		browserTableViewer.setInput(renamer);

		// listener that refreshes the browser when the Renamer updates its
		// names
		Renamer.EventListener refreshBrowserListener = new Renamer.EventListener() {
			@Override
			public void seeEvent(EventType eventType, File file, RenamerRule rule) {
				if (eventType == Renamer.EventType.UpdatedNames) {
					System.out.println("refreshing browser");
					browserTableViewer.refresh(true);
				}
			}
		};
		renamer.addEventListener(refreshBrowserListener);

	}

	/**
	 * Add listeners that will monitor and affect the state of the browser.
	 * Buttons, Text Fields, etc.
	 */
	private void addListeners() {
		undoButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renamer.undoRenamerEvent();
			}
		});
		redoButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renamer.redoRenamerEvent();
			}
		});
		// create the "Up one level" listener
		browserUpLevelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				File parentFile = renamer.getCurrentDirectory().getParentFile();
				if (parentFile != null) {
					urlHistory.visitLocation(parentFile.getAbsolutePath());
					sendBrowserToLocation(parentFile);
				}
			}
		});

		// create the "Back" listener
		historyBackButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				urlHistory.regressHistory();
				String newLocation = urlHistory.getCurrentLocation();
				sendBrowserToLocation(new File(newLocation));
			}
		});

		historyForwardButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				urlHistory.progressHistory();
				String newLocation = urlHistory.getCurrentLocation();
				sendBrowserToLocation(new File(newLocation));
			}
		});

		// add a listener that will change the color of the text based on
		// whether the text represents a valid directory
		currentURLText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String userText = currentURLText.getText();
				File maybeFile = new File(userText);
				if (maybeFile.isDirectory()) {
					currentURLText.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				} else {
					currentURLText.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
		});

		// add an "Enter" listener that will send the browser to the
		// user-entered directory if it is valid
		currentURLText.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == 13 || e.character == 10) {
					String userText = currentURLText.getText();
					if (!userText.equals(urlHistory.getCurrentLocation())) {
						File maybeFolder = new File(userText);
						if (maybeFolder.isDirectory()) {
							urlHistory.visitLocation(maybeFolder.getAbsolutePath());
							sendBrowserToLocation(maybeFolder);
						}
					}
					e.doit = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String newUrl = new DirectoryDialog(browseButton.getShell()).open();
				if (newUrl != null) {
					urlHistory.visitLocation(newUrl);
					sendBrowserToLocation(new File(newUrl));
				}
			}
		});

		browserTableViewer.getTable().addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ViewerCell selectedCell = browserTableViewer.getCell(new Point(e.x, e.y));
				if (selectedCell == null) {
					return;
				}
				Object element = selectedCell.getElement();
				if (element == null || !(element instanceof File)) {
					return;
				}
				File clickedFile = (File) element;
				if (clickedFile.isDirectory()) {
					urlHistory.visitLocation(clickedFile.getAbsolutePath());
					sendBrowserToLocation(clickedFile);
				}
			}
		});

		SelectionAdapter applyChangesListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Clicked apply changes");
				renamer.applyChanges();
				// browserTableViewer.setInput(renamer);
			}
		};
		applyButton.addSelectionListener(applyChangesListener);
	}

	/**
	 * Sets the renamer's current location and refreshes the browser viewer.
	 * Adds the location to the visited locations
	 * 
	 * @param location
	 */
	private void sendBrowserToLocation(File location) {
		if (location != null) {
			renamer.setCurrentDirectory(location);
			browserTableViewer.refresh();

			String url = location.getAbsolutePath();
			currentURLText.setText(url);
			currentURLText.setSelection(url.length());
			// urlComboViewer.setSelection(selection);
			// urlComboViewer.getCombo().setText(location.getAbsolutePath());
			// urlComboViewer.refresh();

			updateNavigationButtonStatus();

		}
	}

	/**
	 * Update the status of the "Back" and "Forward" buttons so that they will
	 * only be active if the URL history can go in that direction.
	 */
	private void updateNavigationButtonStatus() {
		boolean canGoBack = urlHistory.canRegressHistory();
		boolean canGoFront = urlHistory.canProgressHistory();
		historyBackButton.setEnabled(canGoBack);
		historyForwardButton.setEnabled(canGoFront);
	}

	@Override
	public void setFocus() {
		// nothing needs to happen here
	}

}
