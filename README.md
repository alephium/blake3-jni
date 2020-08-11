## Install
1. [BLAKE3](https://github.com/BLAKE3-team/BLAKE3) repository is used as a git submodule
```
git submodule init
git submodule update
```
2. Generate C header file
```
javac src/main/java/Blake3Jni.java -h src/main/c
```
3. Build the shared library:
  * Linux (`libblake3.so`):
```
gcc -z noexecstack -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -IBLAKE3/c -fPIC -shared -o libblake3.so src/main/c/Blake3Jni.c BLAKE3/c/blake3.c BLAKE3/c/blake3_dispatch.c BLAKE3/c/blake3_portable.c BLAKE3/c/blake3_sse41_x86-64_unix.S BLAKE3/c/blake3_avx2_x86-64_unix.S BLAKE3/c/blake3_avx512_x86-64_unix.S
```
you can also also run `./make` to perform step 1 and 2 at once.
## Tests
Run the tests with [sbt](https://github.com/sbt/sbt):
```
sbt -Djava.library.path=$(pwd) test
```
