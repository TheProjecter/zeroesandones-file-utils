package edu.zao.fire.editors.metadata;

import java.util.Map;
import java.util.TreeMap;

import org.jaudiotagger.tag.FieldKey;

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
			return MetadataTag.makePlainTextTag("-");
		}
		return new MetadataTag(tagName, "Unknown " + tagName);
	}

	private final static Map<String, FieldKey> fieldKeyMap = new TreeMap<String, FieldKey>();
	static {
		fieldKeyMap.put(MetadataTagNames.ALBUM, FieldKey.ALBUM);
		fieldKeyMap.put(MetadataTagNames.ARTIST, FieldKey.ARTIST);
		fieldKeyMap.put(MetadataTagNames.COMMENT, FieldKey.COMMENT);
		fieldKeyMap.put(MetadataTagNames.COMPOSER, FieldKey.COMPOSER);
		fieldKeyMap.put(MetadataTagNames.TITLE, FieldKey.TITLE);
		fieldKeyMap.put(MetadataTagNames.TRACK, FieldKey.TRACK);
		fieldKeyMap.put(MetadataTagNames.YEAR, FieldKey.YEAR);
	}

	public static FieldKey getFieldKey(String keyName) {
		return fieldKeyMap.get(keyName);
	}

}
