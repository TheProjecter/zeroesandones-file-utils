package edu.zao.fire.editors.metadata;

import java.io.Serializable;

import org.jaudiotagger.tag.FieldKey;

public class MetadataTag implements Serializable {
	public final String tagName;
	private String defaultText;
	private final FieldKey fieldKeyForTag;

	public final static MetadataTag ARTIST = new MetadataTag(MetadataTagNames.ARTIST, FieldKey.ARTIST, "Unknown Artist");
	public final static MetadataTag ALBUM = new MetadataTag(MetadataTagNames.ALBUM, FieldKey.ALBUM, "Unknown Album");
	public final static MetadataTag TITLE = new MetadataTag(MetadataTagNames.TITLE, FieldKey.TITLE, "Unknown Title");
	public final static MetadataTag TRACK = new MetadataTag(MetadataTagNames.TRACK, FieldKey.TRACK, "Unknown Track");
	public final static MetadataTag YEAR = new MetadataTag(MetadataTagNames.YEAR, FieldKey.YEAR, "Unknown Year");
	public final static MetadataTag COMMENT = new MetadataTag(MetadataTagNames.COMMENT, FieldKey.COMMENT, "No Comment");
	public final static MetadataTag COMPOSER = new MetadataTag(MetadataTagNames.COMPOSER, FieldKey.COMPOSER, "Unknown Composer");

	public static MetadataTag makePlainTextTag(String text) {
		return new MetadataTag(MetadataTagNames.PLAINTEXT, null, text);
	}

	public MetadataTag(String tagName, FieldKey fieldKeyForTag, String defaultText) {
		this.tagName = tagName;
		this.fieldKeyForTag = fieldKeyForTag;
		this.defaultText = defaultText;
	}

	public FieldKey getFieldKey() {
		return fieldKeyForTag;
	}

	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String newText) {
		defaultText = newText;
	}
}
