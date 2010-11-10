package edu.zao.fire.editors.metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class MetadataTagList {

	// TODO: instead of holding strings, this should eventually be holding some
	// kind of object that represents a metadata tag.

	// CHRIS: maps will work. jaudiotagger lib has a "Tag" interface
	// that will give us this info, we can create an arraylist of maps linking
	// an enum
	// (defined in the "Tag") to its value (defined in
	// Tag.getFirst(FieldKey.VALUE))
	// like "ARTIST" to "(artist name)"
	// we use an arraylist so that we can change the ordering quickly for the
	// renamer
	// and the map as an easy way to keep associations

	private final ArrayList<String> tags = new ArrayList<String>();

	public Object[] getTags() {
		return tags.toArray();
	}

	public void createMetadataMap(File inputSong) {

		final Map<String, String> metadataMap = new TreeMap<String, String>();

		AudioFile song = AudioFileIO.read(inputSong);
		Tag songTag = song.getTag();

		metadataMap.put("Artist", getArtist(songTag));
		metadataMap.put("Album", getAlbum(songTag));
		metadataMap.put("Title", getTitle(songTag));
		metadataMap.put("Track", getTrack(songTag));
		metadataMap.put("Year", getYear(songTag));
		metadataMap.put("Composer", getComposer(songTag));
		metadataMap.put("Comment", getComment(songTag));

	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public String getArtist(Tag songTag) {
		return songTag.getFirst(FieldKey.ARTIST);
	}

	public String getAlbum(Tag songTag) {
		return songTag.getFirst(FieldKey.ALBUM);
	}

	public String getTitle(Tag songTag) {
		return songTag.getFirst(FieldKey.TITLE);
	}

	public String getTrack(Tag songTag) {
		return songTag.getFirst(FieldKey.TRACK);
	}

	public String getYear(Tag songTag) {
		return songTag.getFirst(FieldKey.YEAR);
	}

	public String getComposer(Tag songTag) {
		return songTag.getFirst(FieldKey.COMPOSER);
	}

	public String getComment(Tag songTag) {
		return songTag.getFirst(FieldKey.COMMENT);
	}

}
