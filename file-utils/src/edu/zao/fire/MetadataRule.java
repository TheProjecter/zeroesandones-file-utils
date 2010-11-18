package edu.zao.fire;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

	private final MetadataTagList tagList = new MetadataTagList();

	/**
	 * This is a map that stores a jaudiotagger Tag for each File. The point of
	 * this is that once we use jaudiotagger to get a tag for a file, that tag
	 * will be stored in this cache so that we don't have to go reading a file
	 * every time we want the info (only read the file once, basically).
	 */
	private final Map<File, Tag> songTagCache = new HashMap<File, Tag>();

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

	public Tag getTagFromFile(File inputSong) throws CannotReadException, IOException, TagException, ReadOnlyFileException,
			InvalidAudioFrameException {

		if (!songTagCache.containsKey(inputSong)) {
			AudioFile song = AudioFileIO.read(inputSong);
			Tag songTag = song.getTag();
			songTagCache.put(inputSong, songTag);
		}

		return songTagCache.get(inputSong);

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
