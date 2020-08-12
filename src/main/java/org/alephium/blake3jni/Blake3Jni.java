package org.alephium.blake3jni;

public class Blake3Jni {

    public final static native long allocate_hasher();
    public final static native void delete_hasher(long self);

    public final static native void blake3_hasher_init(long self);
    public final static native void blake3_hasher_init_keyed(long self, byte[] key);
    public final static native void blake3_hasher_init_derive_key(long self, String context);
    public final static native void blake3_hasher_update(long self, byte[] input, int input_len);
    public final static native void blake3_hasher_finalize(long self, byte[] out, int out_len);
    public final static native void blake3_hasher_finalize_seek(long self, long seek, byte[] out, int out_len);
}
