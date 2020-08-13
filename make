#!/bin/sh

if [ ! -e "./lib" ]; then
  mkdir lib
fi;

specific_arg="-z noexecstack"
if uname -a | grep -q -i darwin; then
  specific_arg=""
fi

git submodule init
git submodule update

javac src/main/java/org/alephium/blake3jni/Blake3Jni.java -h src/main/c

gcc $specific_arg -O3 -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -I${JAVA_HOME}/include/darwin -IBLAKE3/c -fPIC -shared -o lib/libblake3.so src/main/c/org_alephium_blake3jni_Blake3Jni.c BLAKE3/c/blake3.c BLAKE3/c/blake3_dispatch.c BLAKE3/c/blake3_portable.c BLAKE3/c/blake3_sse41_x86-64_unix.S BLAKE3/c/blake3_avx2_x86-64_unix.S BLAKE3/c/blake3_avx512_x86-64_unix.S

cp lib/libblake3.so lib/libblake3.dylib
