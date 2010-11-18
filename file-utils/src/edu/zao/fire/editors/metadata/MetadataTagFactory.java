package edu.zao.fire.editors.metadata;

import java.util.Map;
import java.util.TreeMap;

public class MetadataTagFactory {

	private final static Map<String, MetadataTag> singletonTags = new TreeMap<String, MetadataTag>();
	static {
		singletonTags.put(MetadataTagNames.ALBUM, MetadataTag.ALBUM);
		singletonTags.put(MetadataTagNames.ARTIST, MetadataTag.ARTIST);
		singletonTags.put(MetadataTagNames.COMMENT, MetadataTag.COMMENT);
		singletonTags.put(MetadataTagNames.COMPOSER, MetadataTag.COMPOSER);
		singletonTags.put(MetadataTagNames.TITLE, MetadataTag.TITLE);
		singletonTags.put(MetadataTagNames.TRACK, MetadataTag.TRACK);
		singletonTags.put(MetadataTagNames.YEAR, MetadataTag.YEAR);
	}

	public static MetadataTag getTagInstance(String tagName) {
		if (singletonTags.containsKey(tagName)) {
			return singletonTags.get(tagName);
		}
		if (tagName == MetadataTagNames.PLAINTEXT) {
			return MetadataTag.makePlainTextTag("--");
		}
		return new MetadataTag(tagName, null, "Unknown " + tagName);
	}
}
