/*
 * Copyright (C) 2013 Raquel Pau and Albert Coroleu.
 * 
 * Walkmod is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Walkmod is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Walkmod. If
 * not, see <http://www.gnu.org/licenses/>.
 */
/* Generated By:JavaCC: Do not edit this line. TagsParserConstants.java */
package org.walkmod.javalang.tags;

/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface TagsParserConstants {

    /** End of File. */
    int EOF = 0;
    /** RegularExpression Id. */
    int LINKPLAIN = 1;
    /** RegularExpression Id. */
    int LINK = 2;
    /** RegularExpression Id. */
    int VALUE = 3;
    /** RegularExpression Id. */
    int PARAM = 4;
    /** RegularExpression Id. */
    int RETURN = 5;
    /** RegularExpression Id. */
    int THROWS = 6;
    /** RegularExpression Id. */
    int EXCEPTION = 7;
    /** RegularExpression Id. */
    int SEE = 8;
    /** RegularExpression Id. */
    int SERIALFIELD = 9;
    /** RegularExpression Id. */
    int SERIALDATA = 10;
    /** RegularExpression Id. */
    int SERIAL = 11;
    /** RegularExpression Id. */
    int INHERITDOC = 12;
    /** RegularExpression Id. */
    int DOCROOT = 13;
    /** RegularExpression Id. */
    int CODE = 14;
    /** RegularExpression Id. */
    int DEPRECATED = 15;
    /** RegularExpression Id. */
    int AUTHOR = 16;
    /** RegularExpression Id. */
    int LITERAL = 17;
    /** RegularExpression Id. */
    int SINCE = 18;
    /** RegularExpression Id. */
    int VERSION = 19;
    /** RegularExpression Id. */
    int OPENBRACE = 20;
    /** RegularExpression Id. */
    int CLOSEBRACE = 21;
    /** RegularExpression Id. */
    int ASTERISK = 22;
    /** RegularExpression Id. */
    int IDENTIFIER = 23;
    /** RegularExpression Id. */
    int LETTER = 24;
    /** RegularExpression Id. */
    int PART_LETTER = 25;
    /** RegularExpression Id. */
    int NAMECHAR = 26;
    /** RegularExpression Id. */
    int OPERATION = 27;
    /** RegularExpression Id. */
    int WORD = 28;
    /** RegularExpression Id. */
    int SPACE = 29;
    /** RegularExpression Id. */
    int BEGIN = 30;

    /** Lexical state. */
    int DEFAULT = 0;

    /** Literal token values. */
    String[] tokenImage = {"<EOF>", "\"@linkplain\"", "\"@link\"", "\"@value\"", "\"@param\"", "\"@return\"",
            "\"@throws\"", "\"@exception\"", "\"@see\"", "\"@serialField\"", "\"@serialData\"", "\"@serial\"",
            "\"@inheritDoc\"", "\"@docRoot\"", "\"@code\"", "\"@deprecated\"", "\"@author\"", "\"@literal\"",
            "\"@since\"", "\"@version\"", "\"{\"", "\"}\"", "\"*\"", "<IDENTIFIER>", "<LETTER>", "<PART_LETTER>",
            "<NAMECHAR>", "<OPERATION>", "<WORD>", "<SPACE>", "<BEGIN>",};

}
