package com.qc.competition.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.IOException;

/**
 * Created by Antoine on 22/01/2016.
 */


public class JSONMimeTypeDeserializer extends JsonDeserializer<MimeType> {

//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public MimeType deserialize(JsonParser jsonparser,
                                DeserializationContext deserializationcontext) throws IOException {

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        MimeType mimeType = null;
        try {
            mimeType = new MimeType(jsonparser.getValueAsString());
        } catch (MimeTypeParseException e) {
            throw new IOException(e);
        }
        return mimeType;


    }

}
