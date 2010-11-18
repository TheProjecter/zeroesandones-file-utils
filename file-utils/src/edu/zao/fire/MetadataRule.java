package edu.zao.fire;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import edu.zao.fire.editors.metadata.MetadataTag;
import edu.zao.fire.editors.metadata.MetadataTagList;

public class MetadataRule implements RenamerRule {

	private MetadataTagList tagList;

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
			Tag songTag = getTagFromFile(file);
			for (MetadataTag tag : tagList.getTags()) {
				FieldKey key = tag.getFieldKey();
				// pull the values out, tack them on to the newName

				String value = songTag.getFirst(key);
				newName += value;
			}

		} catch (Exception e) {
			return file.getName();
		}

		// pull off the extension, then tack it back on in the end
		newName = newName + extension;
		return newName;
	}

	public Tag getTagFromFile(File inputSong) throws CannotReadException, IOException, TagException, ReadOnlyFileException,
			InvalidAudioFrameException {

		AudioFile song = AudioFileIO.read(inputSong);
		Tag songTag = song.getTag();

		return songTag;

	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tearDown() {
		// TODO Auto-generated method stub

	}

	public MetadataTagList getTagList() {

		return tagList;
	}

	public void setTagList(MetadataTagList tagList) {
		this.tagList = tagList;
	}

}
