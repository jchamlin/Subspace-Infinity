/*
 * Copyright (c) 2018, Asser Fahrenholz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package infinity.map;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Vector;

/**
 * This class represents a region defined by several things: name - rNam isBase
 * - rBSE isNoFlags - rNFL isNoWeps - rNWP isNoAnti - rNAW isAutoWarp - rAWP
 *
 * autoWarp x, y, and arena (in case of autowarp) arena is max 15 letters
 *
 * vector of rectangles - rTIL
 *
 * @author baks
 */
public class Region {
    // constants

    private static int SMALL_EMPTY_RUN = 0;
    private static int LONG_EMPTY_RUN = 1;
    private static int SMALL_PRESENT_RUN = 2;
    private static int LONG_PRESENT_RUN = 3;
    private static int SMALL_EMPTY_ROWS = 4;
    private static int LONG_EMPTY_ROWS = 5;
    private static int SMALL_REPEAT = 6;
    private static int LONG_REPEAT = 7;

    public Color color;
    public String name;

    public boolean isBase = false;
    public boolean isNoFlags = false;
    public boolean isNoWeps = false;
    public boolean isNoAnti = false;
    public boolean isAutoWarp = false;

    // autowarp
    public int x = 512, y = 512;
    public String arena = "";

    public ArrayList<Rectangle> rects = new ArrayList<>();
    public Vector<Byte> unknownBytes = new Vector<>(); // region bytes loaded... but unknown or unused by the program

    public Region() {
        name = "@THIS_IS_A_BUG->ERROR"; // the user should never see this
        color = getRandomColor();
    }

    public Region(final String newName, final Color newColor) {
        name = newName;
        color = newColor;
    }

    /**
     * Get the encoding for this region, save the header
     *
     * @return a Vector of Bytes representing this region
     */
    public Vector<Byte> getEncodedRegion() {
        final Vector<Byte> encoding = new Vector<>();
        // encode isBase
        if (isBase) {
            encoding.add(Byte.valueOf((byte) 'r'));
            encoding.add(Byte.valueOf((byte) 'B'));
            encoding.add(Byte.valueOf((byte) 'S'));
            encoding.add(Byte.valueOf((byte) 'E'));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
        }
        // encode noFlags
        if (isNoFlags) {
            encoding.add(Byte.valueOf((byte) 'r'));
            encoding.add(Byte.valueOf((byte) 'N'));
            encoding.add(Byte.valueOf((byte) 'F'));
            encoding.add(Byte.valueOf((byte) 'L'));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
        }
        // encode noWeps
        if (isNoWeps) {
            encoding.add(Byte.valueOf((byte) 'r'));
            encoding.add(Byte.valueOf((byte) 'N'));
            encoding.add(Byte.valueOf((byte) 'W'));
            encoding.add(Byte.valueOf((byte) 'P'));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
        }
        // encode noAnti
        if (isNoAnti) {
            encoding.add(Byte.valueOf((byte) 'r'));
            encoding.add(Byte.valueOf((byte) 'N'));
            encoding.add(Byte.valueOf((byte) 'A'));
            encoding.add(Byte.valueOf((byte) 'W'));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
            encoding.add(Byte.valueOf((byte) 0));
        }
        // encode isAutoWArp
        if (isAutoWarp) {
            encoding.add(Byte.valueOf((byte) 'r'));
            encoding.add(Byte.valueOf((byte) 'A'));
            encoding.add(Byte.valueOf((byte) 'W'));
            encoding.add(Byte.valueOf((byte) 'P'));
            if (!arena.equals("")) { // encode with arena, size = 20
                final byte[] dword = BitmapSaving.toDWORD(20);
                for (int c = 0; c < 4; ++c) {
                    encoding.add(Byte.valueOf(dword[c]));
                }

                // save x
                byte[] word = BitmapSaving.toWORD(x);

                for (int c = 0; c < 2; ++c) {
                    encoding.add(Byte.valueOf(word[c]));
                }

                // save y
                word = BitmapSaving.toWORD(y);

                for (int c = 0; c < 2; ++c) {
                    encoding.add(Byte.valueOf(word[c]));
                }

                // save arena
                if (arena.length() > 15) {
                    arena = arena.substring(15);
                }

                final int len = arena.length();
                for (int c = 0; c < len; ++c) {
                    encoding.add(Byte.valueOf((byte) arena.charAt(c)));
                }

                for (int c = len; c < 16; ++c) {
                    encoding.add(Byte.valueOf((byte) 0));
                }

            } else { // no arena, size = 4
                final byte[] dword = BitmapSaving.toDWORD(4);
                for (int c = 0; c < 4; ++c) {
                    encoding.add(Byte.valueOf(dword[c]));
                }

                // save x
                byte[] word = BitmapSaving.toWORD(x);

                for (int c = 0; c < 2; ++c) {
                    encoding.add(Byte.valueOf(word[c]));
                }

                // save y
                word = BitmapSaving.toWORD(y);

                for (int c = 0; c < 2; ++c) {
                    encoding.add(Byte.valueOf(word[c]));
                }
            }
        }
        // encode unknown bytes
        for (final Byte unknownByte : unknownBytes) {
            encoding.add(unknownByte);
        }
        // encoded name
        encoding.add(Byte.valueOf((byte) 'r'));
        encoding.add(Byte.valueOf((byte) 'N'));
        encoding.add(Byte.valueOf((byte) 'A'));
        encoding.add(Byte.valueOf((byte) 'M'));

        final int len = name.length();
        byte[] dword = BitmapSaving.toDWORD(len);
        for (int c = 0; c < 4; ++c) {
            encoding.add(Byte.valueOf(dword[c]));
        }

        for (int c = 0; c < len; ++c) {
            encoding.add(Byte.valueOf((byte) name.charAt(c)));
        }

        // pad it
        int padding = 4 - len % 4;
        if (padding != 4) {
            for (int c = 0; c < padding; ++c) {
                encoding.add(Byte.valueOf((byte) 0));
            }
        }

        // encode tiles
        encoding.add(Byte.valueOf((byte) 'r'));
        encoding.add(Byte.valueOf((byte) 'T'));
        encoding.add(Byte.valueOf((byte) 'I'));
        encoding.add(Byte.valueOf((byte) 'L'));

        // we now need the length! yuck! ok let's make another vector containing just
        // the encoding
        final Vector<Byte> tileData = getCompressedRGN();

        dword = BitmapSaving.toDWORD(tileData.size());
        for (int c = 0; c < 4; ++c) {
            encoding.add(Byte.valueOf(dword[c]));
        }

        for (final Byte element : tileData) {
            encoding.add(element);
        }

        // pad it
        padding = 4 - tileData.size() % 4;
        if (padding != 4) {
            for (int c = 0; c < padding; ++c) {
                encoding.add(Byte.valueOf((byte) 0));
            }
        }

        return encoding;
    }

    /**
     * Get the compressed tiledata as a vector of Bytes
     *
     * @return the vector of bytes representing the encoding of this tiledata
     */
    private Vector<Byte> getCompressedRGN() {
        final Vector<Byte> bytes = new Vector<>();

        final boolean rgn[][] = new boolean[1024][1024];

        // start empty
        for (int yPos = 0; yPos < 1024; ++yPos) {
            for (int xPos = 0; xPos < 1024; ++xPos) {
                rgn[yPos][xPos] = false;
            }
        }

        // add rectangles
        for (final Object rect : rects) {
            final Rectangle r = (Rectangle) rect;

            final int endX = r.x + r.width;
            final int endY = r.y + r.height;

            for (int yPos = r.y; yPos < endY; ++yPos) {
                for (int xPos = r.x; xPos < endX; ++xPos) {
                    rgn[yPos][xPos] = true;
                }
            }
        }

        Vector<Byte> lastRow = null;
        int lastRowSameCount = 0;
        int emptyRowCount = 0;

        // okay now we actually have to do the encoding... row by row
        for (int curRow = 0; curRow < 1024; ++curRow) {
            int curY = 0;

            boolean emptyRow = true;
            for (; curY < 1024; ++curY) // check for empty row
            {
                if (rgn[curRow][curY]) {
                    emptyRow = false;
                    break;
                }
            }

            if (emptyRow) {
                emptyRowCount++;

                if (lastRowSameCount > 0) {
                    bytes.addAll(encodeRepeatLastRow(lastRowSameCount));
                }

                lastRow = null;
                lastRowSameCount = 0;

                if (curRow == 1023) // end, encode it
                {
                    bytes.addAll(encodeEmptyRows(emptyRowCount));
                }

                continue;
            }

            if (emptyRowCount > 0) { // encode a number of empty rows
                bytes.addAll(encodeEmptyRows(emptyRowCount));

                emptyRowCount = 0;
            }

            // we have to encode a single row
            final Vector<Byte> encodedRow = new Vector<>();
            curY = 0;
            while (curY < 1024) {
                final boolean encodingTiles = rgn[curRow][curY];

                int count = 0;

                for (; curY < 1024; ++count, ++curY) {
                    if (rgn[curRow][curY] != encodingTiles) {
                        break;
                    }
                }

                // encode count tiles of type encodingTiles
                encodedRow.addAll(encodeRun(count, encodingTiles));
            }

            boolean sameAsLastRow = true;

            if (lastRow == null) {
                sameAsLastRow = false;
            } else if (lastRow.size() != encodedRow.size()) {
                sameAsLastRow = false;
            } else {
                for (int v = 0; v < lastRow.size(); ++v) {
                    if (!lastRow.get(v).equals(encodedRow.get(v))) {
                        sameAsLastRow = false;
                        break;
                    }
                }
            }

            if (sameAsLastRow) {
                lastRowSameCount++;

                if (curRow == 1023) {
                    // save same as Last Row #
                    bytes.addAll(encodeRepeatLastRow(lastRowSameCount));
                }
            } else {
                if (lastRowSameCount != 0) {
                    // save same as Last Row #
                    bytes.addAll(encodeRepeatLastRow(lastRowSameCount));

                    lastRowSameCount = 0;
                }

                bytes.addAll(encodedRow);
                lastRow = encodedRow;
            }

        }

        return bytes;
    }

    /**
     * Encode a number of empty rows
     *
     * @return a Vector of bytes containing the encoding
     */
    private static Vector<Byte> encodeEmptyRows(final int count) {
        /*
         * first, rows that contain no tiles at all can (optionally) be encoded
         * specially:
         *
         * 100n nnnn - n+1 (1-32) rows of all empty 1010 00nn nnnn nnnn - n+1 (1-1024)
         * rows of all empty
         */

        final Vector<Byte> code = new Vector<>();

        int i = count;
        if (i <= 32) {
            i--; // cause it's 1-32 not 0-31
            final byte encode = (byte) (i | 0x80);
            code.add(Byte.valueOf(encode));

        } else {
            i--;
            final byte one = (byte) ((i >> 8) | 0xA0);
            final byte two = (byte) ((i & 0x00FF));

            code.add(Byte.valueOf(one));
            code.add(Byte.valueOf(two));

        }

        return code;
    }

    /**
     * Encode a run
     *
     * @param i        the number of tiles to cover
     * @param inRegion is this a run of region tiles? (or empty spaces)
     * @return the Vector of encodedBytes for this run
     */
    private static Vector<Byte> encodeRun(final int count, final boolean inRegion) {
        /*
         * for each row, split it into runs of empty tiles and present tiles. for each
         * run, output one of these bit sequences:
         *
         * 000n nnnn - n+1 (1-32) empty tiles in a row 0010 00nn nnnn nnnn - n+1
         * (1-1024) empty tiles in a row 010n nnnn - n+1 (1-32) present tiles in a row
         * 0110 00nn nnnn nnnn - n+1 (1-1024) present tiles in a row
         */

        final Vector<Byte> code = new Vector<>();
        int i = count;

        if (i <= 32) {
            --i;
            byte one;

            if (!inRegion) {
                one = (byte) i;
            } else {
                one = (byte) (i | 0x40);
            }

            code.add(Byte.valueOf(one));

        } else {
            --i;
            byte one, two;

            if (!inRegion) // empty tiles
            {
                one = (byte) ((i >> 8) | 0x20);
                two = (byte) (i & 0x00FF);
            } else // present tiles
            {
                one = (byte) ((i >> 8) | 0x60);
                two = (byte) (i & 0x00FF);
            }

            code.add(Byte.valueOf(one));
            code.add(Byte.valueOf(two));

        }

        return code;
    }

    /**
     * encode a repeat of the last row
     *
     * @param count the number of times we repeated
     * @return a Vector of Bytes containing the encoding of this repetition
     */
    private static Vector<Byte> encodeRepeatLastRow(final int count) {
        /*
         * if the same pattern of tiles appears in more than one consecutive row, you
         * can use these special codes to save more space:
         *
         * 110n nnnn - repeat last row n+1 (1-32) times 1110 00nn nnnn nnnn - repeat
         * last row n+1 (1-1024) times
         */

        final Vector<Byte> code = new Vector<>();
        int i = count;
        if (i <= 32) {
            i--; // cause it's 1-32 not 0-31
            final byte encode = (byte) (i | 0xC0);
            code.add(Byte.valueOf(encode));
        } else {
            i--;
            final byte one = (byte) ((i >> 8) | 0xE0);
            final byte two = (byte) ((i & 0x00FF));

            code.add(Byte.valueOf(one));
            code.add(Byte.valueOf(two));
        }

        return code;
    }

    /**
     * Get this byte in binary
     *
     * @param b the byte to convert
     */
    @SuppressWarnings("unused")
    private static String getBinaryStringOfByte(final byte b) {
        int mask = 0x00000080;
        String rv = "";

        for (int x = 0; x < 8; ++x) {
            rv += ((b & mask) == 0 ? "0" : "1");
            mask = mask >> 1;
        }

        return rv;
    }

    /**
     * Load the data in this ByteArray into this region
     *
     * @param encoding an eLVL REGN chunk, without the header
     * @return the error String, or null
     */
    public String decodeRegion(final ByteArray encoding) {
        String error = null;
        final int superChunkLen = encoding.m_array.length;
        int cur = 0;

        while (cur < superChunkLen) {
            if (superChunkLen - cur < 8) // not enough room for subchunk header
            {
                error = "Not enogh bytes to make a subchunk header in REGN superchunk.";
                break;
            }
            final String type = encoding.readString(cur, 4);
            cur += 4;
            final int len = encoding.readLittleEndianInt(cur);
            cur += 4;

            // "rBSE" - whether the region represents a base in a flag game
            if (type.equals("rBSE") && len == 0) {
                isBase = true;
            } // "rNAW" - no antiwarp
            else if (type.equals("rNAW") && len == 0) {
                isNoAnti = true;
            } // "rNWP" - no weapons
            else if (type.equals("rNWP") && len == 0) {
                isNoWeps = true;
            } // "rNFL" - no flag drops
            else if (type.equals("rNFL") && len == 0) {
                isNoFlags = true;
            } // "rAWP" - auto-warp
            else if (type.equals("rAWP")) {
                isAutoWarp = true;
                x = encoding.readLittleEndianShort(cur);
                cur += 2;
                y = encoding.readLittleEndianShort(cur);
                cur += 2;
                if (len == 20) // we also have an arena
                {
                    arena = encoding.readNullTerminatedString(cur);
                    cur += 16;
                }
            } // "rNAM" - a descriptive name for the region
            else if (type.equals("rNAM")) {
                name = encoding.readString(cur, len);
                cur += len;

                final int padding = 4 - len % 4;
                if (padding != 4) {
                    cur += padding;
                }
            } // "rTIL" - tile data, the definition of the region
            else if (type.equals("rTIL")) {
                error = decodeTiles(encoding.m_array, cur, len);

                if (error != null) {
                    break;
                }

                cur += len;

                final int padding = 4 - len % 4;
                if (padding != 4) {
                    cur += padding;
                }
            } // other - unknown tiles, or maybe that python stuff
            else {
                // encode header
                unknownBytes.add(Byte.valueOf((byte) type.charAt(0)));
                unknownBytes.add(Byte.valueOf((byte) type.charAt(1)));
                unknownBytes.add(Byte.valueOf((byte) type.charAt(2)));
                unknownBytes.add(Byte.valueOf((byte) type.charAt(3)));
                final byte[] dword = BitmapSaving.toDWORD(len);
                for (int c = 0; c < 4; ++c) {
                    unknownBytes.add(Byte.valueOf(dword[c]));
                }

                // encode data
                final int endIndex = cur + len;
                for (int c = cur; c < endIndex; ++c) {
                    final byte b = encoding.readByte(c);
                    unknownBytes.add(Byte.valueOf(b));
                }
                cur += len;

                // encode padding
                final int padding = 4 - len % 4;
                if (padding != 4) {
                    unknownBytes.add(Byte.valueOf((byte) 0));
                    cur += padding;
                }
            }
        }

        if (error == null && cur != superChunkLen) {
            error = "REGN chunk eLVL data went past encoded length, cur = " + cur + ", superChunkLength = "
                    + superChunkLen;
        }

        return error;
    }

    /**
     * decode the rTIL section into the vector of rectangles
     *
     * @param data   the byte[] of data loaded
     * @param offset the offset to start reading
     * @param len    the length to read
     * @return the error String
     */
    private String decodeTiles(final byte[] data, final int offset, final int size) {
        String error = null;
        int o = offset;
        final int endByte = o + size;
        final boolean[][] rgn = new boolean[1024][1024];

        // region starts empty
        for (int xPos = 0; xPos < 1024; ++xPos) {
            for (int yPos = 0; yPos < 1024; ++yPos) {
                rgn[xPos][yPos] = false;
            }
        }

        int curX = 0;
        int curY = 0;

        while (o < endByte) {
            final byte typeByte = data[o];

            final int type = getEncodedType(typeByte);
            final int len = getEncodedLength(data, o, type);

            if (type == SMALL_EMPTY_RUN || type == LONG_EMPTY_RUN) {
                if (len + curX > 1024) {
                    error = "empty run extends past end";
                    break;
                }

                curX += len;
            } else if (type == SMALL_PRESENT_RUN || type == LONG_PRESENT_RUN) {
                if (len + curX > 1024) {
                    error = "present run extends past end";
                    break;
                }

                final int stopX = curX + len;

                for (int xPos = curX; xPos < stopX; ++xPos) {
                    rgn[curY][xPos] = true;
                }

                curX += len;
            } else if (type == SMALL_EMPTY_ROWS || type == LONG_EMPTY_ROWS) {
                if (curX != 0) {
                    error = "empty row occured before a run was over, curX = " + curX;
                    break;
                }

                curY += len;
            } else if (type == SMALL_REPEAT || type == LONG_REPEAT) {
                if (curX != 0) {
                    error = "repeat occured before a run was over.";
                    break;
                }
                if (curY == 0) {
                    error = "repeat occured in the first row.";
                    break;
                }

                final int stopY = curY + len;
                final int copyY = curY - 1;

                for (int xPos = 0; xPos < 1024; ++xPos) {
                    for (int yPos = curY; yPos < stopY; ++yPos) {
                        rgn[yPos][xPos] = rgn[copyY][xPos];
                    }
                }

                curY += len;
            }

            if (curX == 1024) {
                ++curY;
                curX = 0;
            }

            if (type % 2 == 0) // short
            {
                ++o;
            } else // long
            {
                o += 2;
            }
        }

        if (error == null && curY != 1024) {
            error = "Encoded rTIL does NOT contain 1024 rows... it has " + curY;
        }

        if (error == null) {
            // at this point we're all encoded, make the rectangles
            curX = curY = 0;

            while (curY < 1024) {
                if (rgn[curY][curX] == true) {
                    final Rectangle r = new Rectangle();
                    r.x = curX;
                    r.y = curY;

                    int w = 1;
                    for (int XPos = curX + 1; XPos < 1024; ++XPos) {
                        if (rgn[curY][XPos] == false) {
                            break;
                        }

                        ++w;
                    }

                    r.width = w;

                    int h = 1;
                    for (int yPos = r.y + 1; yPos < 1024; ++yPos) {
                        final int endX = r.x + r.width;
                        boolean sameRow = true;
                        for (int xPos = r.x; xPos < endX; ++xPos) {
                            if (rgn[yPos][xPos] == false) {
                                sameRow = false;
                                break;
                            }
                        }

                        if (sameRow == false) {
                            break;
                        }

                        ++h;
                    }

                    r.height = h;

                    final int endX = r.x + r.width;
                    final int endY = r.y + r.height;

                    for (int yPos = r.y; yPos < endY; ++yPos) {
                        for (int xPos = r.x; xPos < endX; ++xPos) {
                            rgn[yPos][xPos] = false; // this part has been proccessed
                        }
                    }

                    rects.add(r);
                }

                // increment
                if (++curX == 1024) {
                    curX = 0;
                    ++curY;
                }
            }
        }

        return error;
    }

    /**
     * Get a random Color
     *
     * @return a random Color
     */
    private static Color getRandomColor() {
        int r = (int) (Math.random() * 255);
        int g = (int) (Math.random() * 255);
        int b = (int) (Math.random() * 255);

        if (r + b + g < 40) {
            r += 20;
            g += 20;
            b += 20;
        }

        return new Color(r, g, b);
    }

    /**
     * Get an encoded length for this rTil entry
     *
     * @param data   the data array
     * @param offset the current offset
     * @param type   the type of the fragment at offset in data
     * @return the length that's encoded
     */
    private static int getEncodedLength(final byte[] data, final int offset, final int type) {
        int len = -1;

        if (type % 2 == 0) // short
        {
            len = getEncodedLength(data[offset]);
        } else {
            len = getEncodedLength(data[offset], data[offset + 1]);
        }

        return len;
    }

    /**
     * Get the encoded length in this SMALL one byte rTIL encoding
     *
     * @param one the byte for this encoding
     * @return an in 1-32 represeting the length that's currently encoded
     */
    private static int getEncodedLength(final byte one) {
        return getBitFragment(one, 4, 8) + 1;
    }

    /**
     * Get the encoded length in this LONG two byte rTIL encoding
     *
     * @param one the first byte for this encoding
     * @param two the second byte for this encoding
     * @return an in 1-1024 represeting the length that's currently encoded
     */
    private static int getEncodedLength(final byte one, final byte two) {
        final int highByte = getBitFragment(one, 7, 8) << 8;
        return (highByte | (0xFF & two)) + 1;
    }

    /**
     * Get the type of encoding this is for eLVL rTIL encoding... will be a constant
     * such as Region.SMALL_EMPTY_RUN or Region.LONG_REPEAT
     *
     * @param typeByte
     * @return
     */
    private static int getEncodedType(final byte typeByte) {
        return getBitFragment(typeByte, 1, 3);
    }

    /**
     * get the bit fragment from startIndex to endIndex
     *
     * @param extractFrom the byte to extract from
     * @param startIndex  the inclusive leftbound index: 1234 5678
     * @param endIndex    the inclusive rightbound index 1234 5678 and startIndex
     * @return the int extracted from the requested bits
     */
    public static int getBitFragment(final byte extractFrom, final int startIndex, final int endIndex) {
        final int shift = 8 - endIndex;
        final int numBits = endIndex - startIndex + 1;
        final byte mask = (byte) ((0x01 << numBits) - 1);

        return (extractFrom >> shift) & mask;
    }

}
