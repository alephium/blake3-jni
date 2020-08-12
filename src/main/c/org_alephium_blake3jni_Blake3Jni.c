#include <stdlib.h>

#include "org_alephium_blake3jni_Blake3Jni.h"
#include "blake3.h"

blake3_hasher* hasher_ptr(jlong self)
{
  return (blake3_hasher*)(uintptr_t)self;
}

uint8_t* get_byte_array(JNIEnv *env, jbyteArray array)
{
  return (uint8_t*) (*env)->GetByteArrayElements(env, array, 0);
}

void release_byte_array(JNIEnv *env, jbyteArray array, uint8_t* addr)
{
  return (*env)->ReleaseByteArrayElements(env, array, addr, 0);
}

JNIEXPORT jlong JNICALL Java_org_alephium_blake3jni_Blake3Jni_allocate_1hasher
  (JNIEnv *env, jobject obj)
{
  blake3_hasher *hasher = malloc (sizeof (blake3_hasher));

  return (jlong)hasher;
}

JNIEXPORT void JNICALL Java_org_alephium_blake3jni_Blake3Jni_delete_1hasher
  (JNIEnv *env, jobject obj, jlong self)
{
  blake3_hasher *hasher = hasher_ptr(self);

  if (hasher != NULL) {
    free(hasher);
  }

  return;
}

JNIEXPORT void JNICALL Java_org_alephium_blake3jni_Blake3Jni_blake3_1hasher_1init
  (JNIEnv *env, jobject obj, jlong self)
{
  return blake3_hasher_init(hasher_ptr(self));
}

JNIEXPORT void JNICALL Java_org_alephium_blake3jni_Blake3Jni_blake3_1hasher_1init_1keyed
  (JNIEnv *env, jobject obj, jlong self, jbyteArray key)
{
  uint8_t *key_addr = get_byte_array(env, key);

  blake3_hasher_init_keyed(hasher_ptr(self), key_addr);

  return release_byte_array(env, key, key_addr);
}

JNIEXPORT void JNICALL Java_org_alephium_blake3jni_Blake3Jni_blake3_1hasher_1init_1derive_1key
  (JNIEnv *env, jobject obj, jlong self, jstring context)
{
  const char *ctx = (*env)->GetStringUTFChars(env, context, 0);

  blake3_hasher_init_derive_key(hasher_ptr(self), ctx);

  return (*env)->ReleaseStringUTFChars(env, context, ctx);
}

JNIEXPORT void JNICALL Java_org_alephium_blake3jni_Blake3Jni_blake3_1hasher_1update
  (JNIEnv *env, jobject obj, jlong self, jbyteArray input, jint input_len)
{
  void *input_addr = get_byte_array(env, input);

  blake3_hasher_update(hasher_ptr(self), input_addr, input_len);

  return release_byte_array(env, input, input_addr);
}

JNIEXPORT void JNICALL Java_org_alephium_blake3jni_Blake3Jni_blake3_1hasher_1finalize
  (JNIEnv *env, jobject obj, jlong self, jbyteArray out, jint out_len)
{
  uint8_t *out_addr = get_byte_array(env, out);

  blake3_hasher_finalize(hasher_ptr(self), out_addr, out_len);

  return release_byte_array(env, out, out_addr);
}

JNIEXPORT void JNICALL Java_org_alephium_blake3jni_Blake3Jni_blake3_1hasher_1finalize_1seek
  (JNIEnv *env, jobject obj, jlong self, jlong seek, jbyteArray out, jint out_len)
{
  uint8_t *out_addr = get_byte_array(env, out);

  blake3_hasher_finalize_seek(hasher_ptr(self), seek, out_addr, out_len);

  return release_byte_array(env, out, out_addr);
}
