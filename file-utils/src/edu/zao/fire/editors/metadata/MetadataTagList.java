package edu.zao.fire.editors.metadata;

import java.util.ArrayList;

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

	private final ArrayList<MetadataTag> tags = new ArrayList<MetadataTag>();

	public Object[] getTagsArray() {
		return tags.toArray();
	}

	public Iterable<MetadataTag> getTags() {
		return tags;
	}

	public void addTag(MetadataTag tag) {
		tags.add(tag);
	}
}
