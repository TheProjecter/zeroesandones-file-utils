package edu.zao.fire.editors.metadata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 * Singleton class that stores metadata values for files in a cache-like format.
 * 
 * @author Dylan
 * 
 */
public class MetadataTagCache {

	private static MetadataTagCache instance;

	private MetadataTagCache() {
		// private constructor
	}

	public static MetadataTagCache getInstance() {
		if (instance == null) {
			instance = new MetadataTagCache();
		}
		return instance;
	}

	/**
	 * This is a map that stores a jaudiotagger Tag for each File. The point of
	 * this is that once we use jaudiotagger to get a tag for a file, that tag
	 * will be stored in this cache so that we don't have to go reading a file
	 * every time we want the info (only read the file once, basically).
	 */
	private final Map<File, Tag> songTagCache = new HashMap<File, Tag>();

	public Tag getTagFromFile(File inputSong) throws CannotReadException, IOException, TagException, ReadOnlyFileException,
			InvalidAudioFrameException {

		if (!songTagCache.containsKey(inputSong)) {
			AudioFile song = AudioFileIO.read(inputSong);
			Tag songTag = song.getTag();
			songTagCache.put(inputSong, songTag);
		}

		return songTagCache.get(inputSong);

	}

}
