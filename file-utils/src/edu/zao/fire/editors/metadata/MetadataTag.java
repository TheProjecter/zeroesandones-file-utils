package edu.zao.fire.editors.metadata;

import java.io.Serializable;

import org.jaudiotagger.tag.FieldKey;

public class MetadataTag implements Serializable {
	public final String tagName;
	private String defaultText;

	public final static MetadataTag ARTIST = new MetadataTag(MetadataTagNames.ARTIST, "Unknown Artist");
	public final static MetadataTag ALBUM = new MetadataTag(MetadataTagNames.ALBUM, "Unknown Album");
	public final static MetadataTag TITLE = new MetadataTag(MetadataTagNames.TITLE, "Unknown Title");
	public final static MetadataTag TRACK = new MetadataTag(MetadataTagNames.TRACK, "Unknown Track");
	public final static MetadataTag YEAR = new MetadataTag(MetadataTagNames.YEAR, "Unknown Year");
	public final static MetadataTag COMMENT = new MetadataTag(MetadataTagNames.COMMENT, "No Comment");
	public final static MetadataTag COMPOSER = new MetadataTag(MetadataTagNames.COMPOSER, "Unknown Composer");

	public static MetadataTag makePlainTextTag(String text) {
		return new MetadataTag(MetadataTagNames.PLAINTEXT, text);
	}

	public MetadataTag(String tagName, String defaultText) {
		this.tagName = tagName;
		this.defaultText = defaultText;
	}

	public FieldKey getFieldKey() {
		return MetadataTagFactory.getFieldKey(tagName);
	}

	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String newText) {
		defaultText = newText;
	}
}
