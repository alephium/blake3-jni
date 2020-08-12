## Install
run `./make`

The following steps are executed:

1. [BLAKE3](https://github.com/BLAKE3-team/BLAKE3) repository is used as a git submodule
```
git submodule init
git submodule update
```
2. Generate C header file
```
javac src/main/java/org/alephium/blake3jni/Blake3Jni.java -h src/main/c
```
3. Build the shared library:
  * Linux (`libblake3.so`):
```
clang -z noexecstack -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -I${JAVA_HOME}/include/darwin -IBLAKE3/c -fPIC -shared -o lib/libblake3.so src/main/c/org_alephium_blake3jni_Blake3Jni.c BLAKE3/c/blake3.c BLAKE3/c/blake3_dispatch.c BLAKE3/c/blake3_portable.c BLAKE3/c/blake3_sse41_x86-64_unix.S BLAKE3/c/blake3_avx2_x86-64_unix.S BLAKE3/c/blake3_avx512_x86-64_unix.S
```

## Tests
Run the tests with [sbt](https://github.com/sbt/sbt):
```
sbt test
```
