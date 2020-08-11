#!/bin/sh

git submodule init
git submodule update

javac src/main/java/Blake3Jni.java -h src/main/c

gcc -z noexecstack -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -IBLAKE3/c -fPIC -shared -o libblake3.so src/main/c/Blake3Jni.c BLAKE3/c/blake3.c BLAKE3/c/blake3_dispatch.c BLAKE3/c/blake3_portable.c BLAKE3/c/blake3_sse41_x86-64_unix.S BLAKE3/c/blake3_avx2_x86-64_unix.S BLAKE3/c/blake3_avx512_x86-64_unix.S
