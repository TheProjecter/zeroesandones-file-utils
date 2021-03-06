package edu.zao.fire.views.browser;

import java.io.File;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
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
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.zao.fire.Renamer;
import edu.zao.fire.Renamer.EventListener;
import edu.zao.fire.Renamer.EventType;
import edu.zao.fire.RenamerRule;
import edu.zao.fire.filters.UserIgnoreFileFilter;
import edu.zao.fire.views.browser.urlassist.URLContentProposalProvider;

public class BrowserView extends ViewPart {

	private final Renamer renamer = Renamer.getDefault();

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

	private Button limitDisplayCheck;

	private final BrowserTableContentProvider browserContentProvider = new BrowserTableContentProvider();

	private final UserIgnoreFileFilter userSelectionFileFilter = renamer.getUserFilters().getIndividualFilter();

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
		historyBackButton.setToolTipText("Go to the previously visited location in the file browser");

		// create the "Go Forward" button
		historyForwardButton = new Button(toolbarTopArea, SWT.PUSH);
		historyForwardButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD));
		historyForwardButton.setToolTipText("Go forward in the history of visited locations");

		// create the "Up One Level" button
		browserUpLevelButton = new Button(toolbarTopArea, SWT.PUSH);
		browserUpLevelButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_UP));
		browserUpLevelButton.setToolTipText("Go up one level in the directory structure");

		currentURLText = new Text(toolbarTopArea, SWT.SINGLE | SWT.BORDER);
		currentURLText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		currentURLText.setToolTipText("This area displays the current url. Type and press enter to change\n"
				+ "the URL. Pressing Ctrl+Space will open a list of previously visited\n"
				+ "locations that might complete whatever url has been typed.");

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
		browseButton.setToolTipText("Browse to a new location");

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
		undoButton.setEnabled(false);
		undoButton.setToolTipText("Undo the last 'Apply Changes'");

		redoButton = new Button(bottomBarArea, SWT.PUSH);
		redoButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_REDO));
		redoButton.setEnabled(false);
		redoButton.setToolTipText("Redo the last 'Apply Changes'");

		applyButton = new Button(bottomBarArea, SWT.PUSH);
		applyButton.setText("Apply Changes");
		applyButton.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false));
		applyButton
				.setToolTipText("Apply the changes shown in the browser. All files shown will have their current names replaced with whatever is in the \"New Name\" column.");

		limitDisplayCheck = new Button(bottomBarArea, SWT.CHECK);
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
		browserTableViewer.setContentProvider(browserContentProvider);

		final BrowserTableItemSorter tableItemSorter = new BrowserTableItemSorter();
		browserTableViewer.setSorter(tableItemSorter);

		String[] titles = { "Current Name", "Modified Name" };
		int[] bounds = { 180, 180 };

		for (int index = 0; index < titles.length; index++) {
			final TableViewerColumn column = new TableViewerColumn(browserTableViewer, SWT.LEFT);
			column.getColumn().setText(titles[index]);
			column.getColumn().setWidth(bounds[index]);
			column.getColumn().setResizable(true);
		}

		browserTableViewer.getTable().addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				int tableWidth = browserTableViewer.getTable().getClientArea().width;
				int columnWidth = tableWidth / browserTableViewer.getTable().getColumnCount();
				for (TableColumn column : browserTableViewer.getTable().getColumns()) {
					column.setWidth(columnWidth);
				}
			}
		});

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

		table.setHeaderVisible(true);

		MenuManager tableMenuManager = new MenuManager();
		tableMenuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		table.setMenu(tableMenuManager.createContextMenu(table));

		getSite().registerContextMenu(tableMenuManager, browserTableViewer);
		getSite().setSelectionProvider(browserTableViewer);

		browserTableViewer.setInput(renamer);

		// listener that refreshes the browser when the Renamer updates its
		// names
		Renamer.EventListener refreshBrowserListener = new Renamer.EventListener() {
			@Override
			public void seeEvent(EventType eventType, File file, RenamerRule rule) {
				if (eventType == Renamer.EventType.UpdatedNames) {
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
				updateUndoButtonStatus();
				browserTableViewer.refresh(true);
			}
		});
		redoButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renamer.redoRenamerEvent();
				updateUndoButtonStatus();
				browserTableViewer.refresh(true);
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
				updateUndoButtonStatus();
				// browserTableViewer.setInput(renamer);
			}
		};
		applyButton.addSelectionListener(applyChangesListener);

		limitDisplayCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isFilter = limitDisplayCheck.getSelection();
				if (isFilter) {
					browserContentProvider.addFilter(renamer.changingFileFilter);
				} else {
					browserContentProvider.removeFilter(renamer.changingFileFilter);
				}
				browserTableViewer.refresh(true);
			}
		});
		final ControlDecoration lessThanZeroListNotification = new ControlDecoration(applyButton, SWT.RIGHT | SWT.TOP);
		lessThanZeroListNotification.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR));
		lessThanZeroListNotification.hide();

		final ControlDecoration namingConflictNotification = new ControlDecoration(applyButton, SWT.RIGHT | SWT.TOP);
		namingConflictNotification
				.setDescriptionText("One or more files would have been renamed to the same thing. \nModify your inputs to avoid this happening again.");
		namingConflictNotification.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR));
		namingConflictNotification.hide();

		final ControlDecoration badRegexNotification = new ControlDecoration(applyButton, SWT.RIGHT | SWT.TOP);
		badRegexNotification.setDescriptionText("The regular expression you entered is invalid. Please edit it and try again");
		badRegexNotification.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR));
		badRegexNotification.hide();

		final ControlDecoration ioExceptionNotification = new ControlDecoration(applyButton, SWT.RIGHT | SWT.TOP);
		final String ioExceptionString = "An I/O Error occurred during renaming.";

		ioExceptionNotification.setDescriptionText(ioExceptionString);
		ioExceptionNotification.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_WARNING));
		ioExceptionNotification.hide();

		renamer.addEventListener(new EventListener() {

			@Override
			public void seeEvent(EventType eventType, File file, RenamerRule rule) {
				switch (eventType) {
				case BadRegex:
					badRegexNotification.show();
					applyButton.setEnabled(false);
					break;
				case IOException:
					ioExceptionNotification.setDescriptionText(ioExceptionString);
					ioExceptionNotification.show();
					break;
				case CouldNotRename:
					ioExceptionNotification.setDescriptionText("Could not rename <" + file.getName() + ">");
					ioExceptionNotification.show();
					break;
				case NameConflict:
					namingConflictNotification.show();
					applyButton.setEnabled(false);
					break;
				case RenamedWithNoProblems:
					namingConflictNotification.hide();
					ioExceptionNotification.hide();
					badRegexNotification.hide();
					lessThanZeroListNotification.hide();
					applyButton.setEnabled(true);
					break;
				case LessThanOneRomanList:
					lessThanZeroListNotification.setDescriptionText("Roman numeral lists less than one or greater than 4000 are not allowed."
							+ "\nTry modifying the start from value, or switch ascending/descending style.");
					lessThanZeroListNotification.show();
					applyButton.setEnabled(false);
					break;
				case LessThanZeroList:
					lessThanZeroListNotification
							.setDescriptionText("Lists less than zero are not allowed.\nModify either the start from value or switch to ascending list.");
					lessThanZeroListNotification.show();
					applyButton.setEnabled(false);
				}
			}
		});

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

	private void updateUndoButtonStatus() {
		boolean canUndo = renamer.getRenamerHistory().canUndo();
		boolean canRedo = renamer.getRenamerHistory().canRedo();
		undoButton.setEnabled(canUndo);
		redoButton.setEnabled(canRedo);
	}

	@Override
	public void setFocus() {
		// nothing needs to happen here
	}

}
