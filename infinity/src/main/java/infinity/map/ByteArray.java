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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Internal storage class for holding an array of bytes, and performing various
 * operations on it. Does not account for data that exceeds the intended size of
 * an array.
 */
public class ByteArray {

    byte[] m_array; // Byte data of the array
    int m_pointer = 0; // Index of the current insertion point

    /**
     * Constructs an empty ByteArray.
     */
    public ByteArray() {
    }

    /**
     * Constructs an empty ByteArray of a given size. If size is not ample, problems
     * will occur (not a user-friendly datatype).
     *
     * @param size Size of ByteArray to construct
     */
    public ByteArray(final int size) {

        m_array = new byte[size];
    }

    /**
     * Constructs a new ByteArray using a DatagramPacket.
     *
     * @param packet Packet to use to construct the ByteArray
     */
    public ByteArray(final DatagramPacket packet) {

        m_array = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), 0, m_array, 0, packet.getLength());
    }

    /**
     * Constructs a new ByteArray using an array of bytes.
     *
     * @param byteArray Array of bytes
     */
    public ByteArray(final byte[] byteArray) {

        m_array = new byte[byteArray.length];

        addByteArray(byteArray);
    }

    /**
     * Constructs a new ByteArray using an array of ints.
     *
     * @param intArray Array of ints
     */
    public ByteArray(final int[] intArray) {

        m_array = new byte[intArray.length];

        addByteArray(intArray);
    }

    /**
     * @return Size/length of the ByteArray
     */
    public int size() {
        return m_array.length;
    }

    /**
     * Enlarge the array to a given size.
     *
     * @param newSize Size to enlarge to
     */
    public void growArray(final int newSize) {
        final byte[] newarray = new byte[newSize];

        System.arraycopy(m_array, 0, newarray, 0, m_array.length);
        m_array = newarray;
    }

    /**
     * Shrink the array to a given size.
     *
     * @param newSize Size to shrink to
     */
    public void shrinkArray(final int newSize) {
        final byte[] newarray = new byte[newSize];

        System.arraycopy(m_array, 0, newarray, 0, newSize);
        m_array = newarray;
    }

    /**
     * Set the index of the array pointer to a specified point.
     *
     * @param newPointer Index to set to
     */
    public void setPointerIndex(final int newPointer) {

        m_pointer = newPointer;
    }

    /**
     * @return Index of the array pointer
     */
    public int getPointerIndex() {

        return m_pointer;
    }

    /**
     * Increment the index pointer by 1.
     */
    public void incrementPointer() {

        m_pointer++;
    }

    /**
     * Decrement the index pointer by 1.
     */
    public void decrementPointer() {
        if (m_pointer != 0) {
            m_pointer++;
        }
    }

    // ***** VARIOUS DATATYPE ADDERS (self-explanatory) *****
    public void addByte(final byte theByte) {
        m_array[m_pointer++] = theByte;
    }

    public void addByte(final byte theByte, final int index) {
        m_array[index] = theByte;
    }

    public void addByte(final int theByte) {
        m_array[m_pointer++] = (byte) ((theByte) & 0xff);
    }

    public void addByte(final int theByte, final int index) {
        m_array[index] = (byte) ((theByte) & 0xff);
    }

    public void addShort(final short theShort) {
        m_array[m_pointer++] = (byte) ((theShort >> 8) & 0xff);
        m_array[m_pointer++] = (byte) ((theShort) & 0xff);
    }

    public void addShort(final short theShort, final int index) {
        m_array[index] = (byte) ((theShort >> 8) & 0xff);
        m_array[index + 1] = (byte) ((theShort) & 0xff);
    }

    public void addLittleEndianShort(final short theShort) {
        m_array[m_pointer++] = (byte) ((theShort) & 0xff);
        m_array[m_pointer++] = (byte) ((theShort >> 8) & 0xff);
    }

    public void addLittleEndianShort(final short theShort, final int index) {
        m_array[index] = (byte) ((theShort) & 0xff);
        m_array[index + 1] = (byte) ((theShort >> 8) & 0xff);
    }

    public void addInt(final int theInt) {
        m_array[m_pointer++] = (byte) ((theInt >> 24) & 0xff);
        m_array[m_pointer++] = (byte) ((theInt >> 16) & 0xff);
        m_array[m_pointer++] = (byte) ((theInt >> 8) & 0xff);
        m_array[m_pointer++] = (byte) ((theInt) & 0xff);
    }

    public void addInt(final int theInt, final int index) {
        m_array[index] = (byte) ((theInt >> 24) & 0xff);
        m_array[index + 1] = (byte) ((theInt >> 16) & 0xff);
        m_array[index + 2] = (byte) ((theInt >> 8) & 0xff);
        m_array[index + 3] = (byte) ((theInt) & 0xff);
    }

    public void addLittleEndianInt(final int theInt) {
        m_array[m_pointer++] = (byte) ((theInt) & 0xff);
        m_array[m_pointer++] = (byte) ((theInt >> 8) & 0xff);
        m_array[m_pointer++] = (byte) ((theInt >> 16) & 0xff);
        m_array[m_pointer++] = (byte) ((theInt >> 24) & 0xff);
    }

    public void addLittleEndianInt(final int theInt, final int index) {
        m_array[index] = (byte) ((theInt) & 0xff);
        m_array[index + 1] = (byte) ((theInt >> 8) & 0xff);
        m_array[index + 2] = (byte) ((theInt >> 16) & 0xff);
        m_array[index + 3] = (byte) ((theInt >> 24) & 0xff);
    }

    public void addLong(final long theLong) {
        m_array[m_pointer++] = (byte) ((theLong >> 56) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 48) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 40) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 32) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 24) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 16) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 8) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong) & 0xff);
    }

    public void addLong(final long theLong, final int index) {
        m_array[index] = (byte) ((theLong >> 56) & 0xff);
        m_array[index + 1] = (byte) ((theLong >> 48) & 0xff);
        m_array[index + 2] = (byte) ((theLong >> 40) & 0xff);
        m_array[index + 3] = (byte) ((theLong >> 32) & 0xff);
        m_array[index + 4] = (byte) ((theLong >> 24) & 0xff);
        m_array[index + 5] = (byte) ((theLong >> 16) & 0xff);
        m_array[index + 6] = (byte) ((theLong >> 8) & 0xff);
        m_array[index + 7] = (byte) ((theLong) & 0xff);
    }

    public void addLittleEndianLong(final long theLong) {
        m_array[m_pointer++] = (byte) ((theLong) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 8) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 16) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 24) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 32) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 40) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 48) & 0xff);
        m_array[m_pointer++] = (byte) ((theLong >> 56) & 0xff);
    }

    public void addLittleEndianLong(final long theLong, final int index) {
        m_array[index] = (byte) ((theLong) & 0xff);
        m_array[index + 1] = (byte) ((theLong >> 8) & 0xff);
        m_array[index + 2] = (byte) ((theLong >> 16) & 0xff);
        m_array[index + 3] = (byte) ((theLong >> 24) & 0xff);
        m_array[index + 4] = (byte) ((theLong >> 32) & 0xff);
        m_array[index + 5] = (byte) ((theLong >> 40) & 0xff);
        m_array[index + 6] = (byte) ((theLong >> 48) & 0xff);
        m_array[index + 7] = (byte) ((theLong >> 56) & 0xff);
    }

    public void addString(final String str) {
        byte[] bytearr;

        try {
            bytearr = str.getBytes("ISO-8859-1");
        } catch (final UnsupportedEncodingException e) {
            bytearr = str.getBytes();
        }

        System.arraycopy(bytearr, 0, m_array, m_pointer, bytearr.length);
        m_pointer += bytearr.length;
    }

    public void addString(final String str, final int index) {
        final byte[] bytearr = str.getBytes();

        System.arraycopy(bytearr, 0, m_array, index, bytearr.length);
    }

    public void addPaddedString(final String str, final int totalLength) {
        if (str == null) {
            repeatAdd(0x0, totalLength);
        } else {
            final byte[] bytearr = str.getBytes();

            System.arraycopy(bytearr, 0, m_array, m_pointer, bytearr.length);
            m_pointer += bytearr.length;

            if (totalLength > str.length()) {
                repeatAdd(0x0, totalLength - str.length());
            }
        }
    }

    public void addPaddedString(final String str, final int index, final int totalLength) {
        if (str == null) {
            repeatAdd(0x0, totalLength, index);
        } else {
            final byte[] bytearr = str.getBytes();

            System.arraycopy(bytearr, 0, m_array, index, bytearr.length);

            if (totalLength > str.length()) {
                repeatAdd(0x0, totalLength - str.length(), index);
            }
        }
    }

    public void addByteArray(final ByteArray byteArray) {
        final byte[] tempArray = byteArray.getByteArray();

        System.arraycopy(tempArray, 0, m_array, m_pointer, tempArray.length);

        m_pointer += tempArray.length;
    }

    public void addByteArray(final ByteArray byteArray, final int index) {
        final byte[] tempArray = byteArray.getByteArray();

        System.arraycopy(tempArray, 0, m_array, index, tempArray.length);
    }

    public void addByteArray(final byte[] byteArray) {

        System.arraycopy(byteArray, 0, m_array, m_pointer, byteArray.length);

        m_pointer += byteArray.length;
    }

    public void addByteArray(final byte[] byteArray, final int index) {

        System.arraycopy(byteArray, 0, m_array, index, byteArray.length);
    }

    public void addByteArray(final int[] intArray) {

        for (final int element : intArray) {
            m_array[m_pointer++] = (byte) ((element) & 0xff);
        }

        m_pointer += intArray.length;
    }

    public void addByteArray(final int[] intArray, final int index) {

        for (int i = 0; i < intArray.length; i++) {
            m_array[index + i] = (byte) ((intArray[i]) & 0xff);
        }
    }

    /**
     * Add contents of a file to the internal byte array.
     *
     * @param file the file to append
     * @throws IOException if file is unavailable
     */
    public void addFileContents(final File file) throws IOException {
        int length;
        byte buffer[];
        FileInputStream fileReader;

        length = (int) file.length();
        buffer = new byte[length];

        fileReader = new FileInputStream(file);
        fileReader.read(buffer, 0, length);
        addByteArray(buffer);
        fileReader.close();
    }

    /**
     * Adds part of another ByteArray to this one.
     *
     * @param byteArray   ByteArray to add
     * @param sourceIndex Index to start the copyover
     * @param length      Size/length of the copy
     */
    public void addPartialByteArray(final ByteArray byteArray, final int sourceIndex, final int length) {
        final byte[] tempArray = byteArray.getByteArray();

        System.arraycopy(tempArray, sourceIndex, m_array, m_pointer, length);
        m_pointer += length;
    }

    /**
     * Adds part of another ByteArray to this one.
     *
     * @param byteArray   ByteArray to add
     * @param destIndex   Index to copy into in the source ByteArray
     * @param sourceIndex Index to start the copyover
     * @param length      Size/length of the copy
     */
    public void addPartialByteArray(final ByteArray byteArray, final int destIndex, final int sourceIndex,
            final int length) {
        final byte[] tempArray = byteArray.getByteArray();

        System.arraycopy(tempArray, sourceIndex, m_array, destIndex, length);
    }

    /**
     * Adds part of a byte array to this one.
     *
     * @param byteArray   byte array to add
     * @param sourceIndex Index to start the copyover
     * @param length      Size/length of the copy
     */
    public void addPartialByteArray(final byte[] byteArray, final int sourceIndex, final int length) {

        System.arraycopy(byteArray, sourceIndex, m_array, m_pointer, length);
        m_pointer += length;
    }

    /**
     * Adds part of a byte array to this one.
     *
     * @param byteArray   byte array to add
     * @param destIndex   Index to copy into in the source ByteArray
     * @param sourceIndex Index to start the copyover
     * @param length      Size/length of the copy
     */
    public void addPartialByteArray(final byte[] byteArray, final int destIndex, final int sourceIndex,
            final int length) {

        System.arraycopy(byteArray, sourceIndex, m_array, destIndex, length);
    }

    /**
     * Adds part of an int array to this one.
     *
     * @param intArray    int array to add
     * @param sourceIndex Index to start the copyover
     * @param length      Size/length of the copy
     */
    public void addPartialByteArray(final int[] intArray, final int sourceIndex, final int length) {

        for (int i = 0; i < length; i++) {
            m_array[m_pointer++] = (byte) ((intArray[i + sourceIndex]) & 0xff);
        }
    }

    /**
     * Adds part of an int array to this one.
     *
     * @param intArray    int array to add
     * @param destIndex   Index to copy into in the source ByteArray
     * @param sourceIndex Index to start the copyover
     * @param length      Size/length of the copy
     */
    public void addPartialByteArray(final int[] intArray, final int destIndex, final int sourceIndex,
            final int length) {

        for (int i = 0; i < length; i++) {
            m_array[destIndex + i] = (byte) ((intArray[i + sourceIndex]) & 0xff);
        }
    }

    // ***** repeatAdd methods *****
    public void repeatAdd(final byte theByte, final int number) {
        for (int i = 0; i < number; i++) {
            m_array[m_pointer++] = theByte;
        }
    }

    public void repeatAdd(final byte theByte, final int number, final int index) {
        for (int i = 0; i < number; i++) {
            m_array[index + i] = theByte;
        }
    }

    public void repeatAdd(final int theInt, final int number) {
        for (int i = 0; i < number; i++) {
            m_array[m_pointer++] = (byte) (theInt & 0xff);
        }
    }

    public void repeatAdd(final int theInt, final int number, final int index) {
        for (int i = 0; i < number; i++) {
            m_array[index + i] = (byte) (theInt & 0xff);
        }
    }

    /**
     * @return Internal byte array inside this object
     */
    public byte[] getByteArray() {
        return m_array;
    }

    // ***** read methods *****
    public byte readByte(final int index) {
        return m_array[index];
    }

    public short readShort(final int index) {
        return (short) (((m_array[index] & 0xff) << 8) | ((m_array[index + 1] & 0xff)));
    }

    public short readLittleEndianShort(final int index) {
        return (short) (((m_array[index + 1] & 0xff) << 8) | ((m_array[index] & 0xff)));
    }

    public int readInt(final int index) {
        return ((m_array[index] & 0xff) << 24) | ((m_array[index + 1] & 0xff) << 16)
                | ((m_array[index + 2] & 0xff) << 8) | ((m_array[index + 3] & 0xff));
    }

    public int readLittleEndianInt(final int index) {
        return ((m_array[index + 3] & 0xff) << 24) | ((m_array[index + 2] & 0xff) << 16)
                | ((m_array[index + 1] & 0xff) << 8) | ((m_array[index] & 0xff));
    }

    public long readLong(final int index) {
        return ((m_array[index] & 0xff) << 56) | ((m_array[index + 1] & 0xff) << 48)
                | ((m_array[index + 2] & 0xff) << 40) | ((m_array[index + 3] & 0xff) << 32)
                | ((m_array[index + 4] & 0xff) << 24) | ((m_array[index + 5] & 0xff) << 16)
                | ((m_array[index + 6] & 0xff) << 8) | ((m_array[index + 7] & 0xff));
    }

    public long readLittleEndianLong(final int index) {
        return ((m_array[index + 7] & 0xff) << 56) | ((m_array[index + 6] & 0xff) << 48)
                | ((m_array[index + 5] & 0xff) << 40) | ((m_array[index + 4] & 0xff) << 32)
                | ((m_array[index + 3] & 0xff) << 24) | ((m_array[index + 2] & 0xff) << 16)
                | ((m_array[index + 1] & 0xff) << 8) | ((m_array[index] & 0xff));
    }

    public String readString(final int index, final int length) {
        Charset targetSet;
        String result = "";

        try {
            targetSet = Charset.forName("ISO-8859-1");
        } catch (final UnsupportedCharsetException uce) {
            targetSet = Charset.defaultCharset();
            // TODO: Log this instead
            // Tools.printLog("Unsupported charset used when decoding string (index=" +
            // index + ",length=" + length + ") from bytearray: " + uce.getMessage());
        }

        result = targetSet.decode(ByteBuffer.wrap(m_array, index, length)).toString().trim();

        return result;
    }

    public String readNullTerminatedString(final int index) {
        int i = 0;

        while (m_array[index + i] != '\0') {
            i++;
        }

        return readString(index, i);
    }

    public ByteArray readByteArray(final int begin, final int end) {
        final byte[] newarray = new byte[end - begin + 1];

        for (int i = 0; i < end + 1 - begin; i++) {
            newarray[i] = m_array[i + begin];
        }

        return new ByteArray(newarray);
    }

    // ***** show methods *****
    /**
     * Sends contents of a passed byte array to the console, in hex format.
     *
     * @param b byte array to display
     */
    public static void show(final byte[] b) {
        for (final byte element : b) {
            System.out.print(ConvertHex.byteToHex(element) + " ");
        }

        System.out.println();
    }

    /**
     * Sends short contents of a passed byte array to the console, in hex format.
     *
     * @param b byte array to display
     */
    public static void showShort(final byte[] b) {
        for (int i = 0; i < b.length && i < 26; i++) {
            System.out.print(ConvertHex.byteToHex(b[i]) + " ");
        }

        System.out.println();
    }

    /**
     * Sends short contents of a passed ByteArray to the console, in hex format.
     *
     * @param byteArray ByteAray to display
     */
    public static void showShort(final ByteArray byteArray) {
        final byte[] b = byteArray.getByteArray();

        for (int i = 0; i < b.length && i < 26; i++) {
            System.out.print(ConvertHex.byteToHex(b[i]) + " ");
        }

        System.out.println();
    }

    /**
     * Sends contents of the internal byte array to the console, in hex format.
     */
    public void show() {
        for (final byte element : m_array) {
            System.out.print(ConvertHex.byteToHex(element) + " ");
        }

        System.out.println();
    }

    /**
     * Sends short contents of the internal byte array to the console, in hex
     * format.
     */
    public void showShort() {
        for (int i = 0; i < m_array.length && i < 26; i++) {
            System.out.print(ConvertHex.byteToHex(m_array[i]) + " ");
        }
    }

    /**
     * Returns an int array as a byte array.
     *
     * @param intarray int array to process
     * @return byte array containing same data
     */
    public static byte[] toByteArray(final int[] intarray) {
        final byte[] barray = new byte[intarray.length];

        for (int i = 0; i < intarray.length; i++) {
            barray[i] = (byte) ((intarray[i]) & 0xff);
        }

        return barray;
    }

    /**
     * Gets the bit fragment from startIndex to endIndex. The indices are being read
     * from right to left.
     *
     * @param extractFrom The byte to extract from
     * @param leftIndex   The inclusive leftbound index: 7654 3210
     * @param rightIndex  The inclusive rightbound index: 7654 3210 and &lt;=
     *                    leftIndex
     * @return The int extracted from the requested bits
     */
    public static int getPartial(final byte extractFrom, final int leftIndex, final int rightIndex) {
        final int numBits = leftIndex - rightIndex + 1;
        final byte mask = (byte) ((0x01 << numBits) - 1);

        return (extractFrom >> rightIndex) & mask;
    }

    /**
     * Gets the bit fragment from startIndex to endIndex. The indices are being read
     * from right to left.
     *
     * @param extractFrom The char to extract from
     * @param leftIndex   The inclusive leftbound index:
     *                    <p>
     *                    15..8 7..0
     *                    </p>
     * @param rightIndex  The inclusive rightbound index:
     *                    <p>
     *                    15..8 7..0
     *                    </p>
     *                    and &lt;= leftIndex
     * @return The int extracted from the requested bits
     */
    public static int getPartial(final char extractFrom, final int leftIndex, final int rightIndex) {
        final int numBits = leftIndex - rightIndex + 1;
        final byte mask = (byte) ((0x01 << numBits) - 1);

        return (extractFrom >> rightIndex) & mask;
    }

    /**
     * Get the bit fragment from startIndex to endIndex. The indices are being read
     * from right to left.
     *
     * @param extractFrom The short to extract from.
     * @param leftIndex   The inclusive leftbound index: 15..8 7..0
     * @param rightIndex  The inclusive rightbound index: 15..8 7..0 &lt;= leftIndex
     * @return The int extracted from the requested bits.
     */
    public static int getPartial(final short extractFrom, final int leftIndex, final int rightIndex) {
        final int numBits = leftIndex - rightIndex + 1;
        final byte mask = (byte) ((0x01 << numBits) - 1);

        return (extractFrom >> rightIndex) & mask;
    }

    /**
     * Get the bit fragment from startIndex to endIndex. The indices are being read
     * from right to left.
     *
     * @param extractFrom The int to extract from.
     * @param leftIndex   The inclusive leftbound index: 31..16 15..0
     * @param rightIndex  The inclusive rightbound index: 31..16 15..0 and &lt;=
     *                    leftIndex
     * @return The int extracted from the requested bits.
     */
    public static int getPartial(final int extractFrom, final int leftIndex, final int rightIndex) {
        final int numBits = leftIndex - rightIndex + 1;
        final byte mask = (byte) ((0x01 << numBits) - 1);

        return (extractFrom >> rightIndex) & mask;
    }

    /**
     * Get the bit fragment from startIndex to endIndex. The indices are being read
     * from right to left.
     *
     * @param extractFrom The long to extract from.
     * @param leftIndex   The inclusive leftbound index: 63..32 31..0
     * @param rightIndex  The inclusive rightbound index: 63..32 31..0 and &lt;=
     *                    leftIndex
     * @return The long extracted from the requested bits.
     */
    public static long getPartial(final long extractFrom, final int leftIndex, final int rightIndex) {
        final int numBits = leftIndex - rightIndex + 1;
        final byte mask = (byte) ((0x01 << numBits) - 1);

        return (extractFrom >> rightIndex) & mask;
    }

    @Override
    public String toString() {
        return new String(m_array);
    }
}
