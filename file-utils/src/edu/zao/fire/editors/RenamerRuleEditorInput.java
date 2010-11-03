package edu.zao.fire.editors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import edu.zao.fire.RenamerRule;

public abstract class RenamerRuleEditorInput implements IEditorInput {

	protected File saveFile = null;

	public abstract String getDefaultName();

	public String getName() {
		if (saveFile == null) {
			return getDefaultName();
		}
		return saveFile.getName();
	};

	public void saveRuleToFile(RenamerRule rule, File file) throws FileNotFoundException, IOException {
		saveFile = file;
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(rule);
		oos.close();
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

}
