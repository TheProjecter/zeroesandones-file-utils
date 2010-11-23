package edu.zao.fire;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import edu.zao.fire.editors.metadata.MetadataTag;
import edu.zao.fire.editors.metadata.MetadataTagCache;
import edu.zao.fire.editors.metadata.MetadataTagList;

public class MetadataRule implements RenamerRule {

	private final MetadataTagList tagList = new MetadataTagList();

	@Override
	public String getNewName(File file) throws IOException {
		String fileName = file.getName();
		String newName = "";
		String extension = "";

		// getlastindexof? make sure to keep the extension name, so look for the
		// '.'
		int dotIndex = fileName.lastIndexOf(".");
		if (dotIndex >= 0) {
			extension = fileName.substring(dotIndex);
		}

		// HERE call the methods to generate the newName
		try {
			MetadataTagCache tagCache = MetadataTagCache.getInstance();
			Tag songTag = tagCache.getTagFromFile(file);

			for (MetadataTag tag : tagList.getTags()) {
				FieldKey key = tag.getFieldKey();
				// pull the values out, tack them on to the newName

				String value;
				// if the metadat is empty, or the tag key is null, use the
				// tag's default text
				if (key == null) {
					value = tag.getDefaultText();
				} else {
					value = songTag.getFirst(key);
					if (value.isEmpty()) {
						value = tag.getDefaultText();
					}
				}

				newName += value;
			}

		} catch (Exception e) {
			return file.getName();
		}

		// pull off the extension, then tack it back on in the end
		newName = newName + extension;
		return newName;
	}

	@Override
	public void setup() {
	}

	@Override
	public void tearDown() {
	}

	public MetadataTagList getTagList() {
		return tagList;
	}
}
