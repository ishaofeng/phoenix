/*******************************************************************************
 * Copyright (c) 2013, Salesforce.com, Inc.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *     Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *     Neither the name of Salesforce.com nor the names of its contributors may 
 *     be used to endorse or promote products derived from this software without 
 *     specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.salesforce.phoenix.schema;

import org.apache.hadoop.hbase.io.ImmutableBytesWritable;


/**
 * Utility methods related to transparent salting of row keys.
 */
public class SaltingUtil {

    public static final Integer MAX_BUCKET_NUM = 256; // Unsigned byte.
    public static final String SALTING_COLUMN_NAME = "_SALT";
    public static final PColumnImpl SALTING_COLUMN = new PColumnImpl(
            new PNameImpl(SALTING_COLUMN_NAME), null, PDataType.CHAR, 1, 0, false, 0, null);

    // Compute the hash of the key value stored in key and set its first byte as the value. The
    // first byte of key should be left empty as a place holder for the salting byte.
    public static byte[] getSaltedKey(ImmutableBytesWritable key, int bucketNum) {
        byte[] keyBytes = new byte[key.getLength()];
        byte saltByte = getSaltingByte(key.get(), key.getOffset() + 1, key.getLength() - 1, bucketNum);
        keyBytes[0] = saltByte;
        System.arraycopy(key.get(), key.getOffset() + 1, keyBytes, 1, key.getLength() - 1);
        return keyBytes;
    }

    // Generate the bucket byte given a byte array and the number of buckets.
    public static byte getSaltingByte(byte[] value, int offset, int length, int bucketNum) {
        int hash = hashCode(value, offset, length);
        byte bucketByte = (byte) ((Math.abs(hash) % bucketNum));
        return bucketByte;
    }

    private static int hashCode(byte a[], int offset, int length) {
        if (a == null)
            return 0;
        int result = 1;
        for (int i = offset; i < offset + length; i++) {
            result = 31 * result + a[i];
        }
        return result;
    }
}