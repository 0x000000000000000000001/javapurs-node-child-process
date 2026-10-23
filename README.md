# purescript-node-child-process

Java port of the PureScript `node-child-process` package. See the
[port maintenance guide](../javapurs/docs/port-maintenance.md)
for source layout, local dependency selection and JVM validation.

## JVM status

The Java adapter implements `spawn`/`spawn'` with `java.lang.ProcessBuilder`, piped
stdout/stderr, `exit`/`close`/`error` events, and the basic process properties.
A focused probe spawning `/bin/sh -c 'printf child-ok'` passes. The full Spec
integration suite is covered by M32: 70/70 checks (62 pure Spec + 8 integration
cases), with subprocess timeouts and late `exit` event replay. `spawnFile` and
`spawnArgs` are preserved; `exec`, `execFile`, `spawnSync`, IPC and `send` fail
with an explicit `UnsupportedOperationException` naming the API.

[![Latest release](http://img.shields.io/github/release/purescript-node/purescript-node-child-process.svg)](https://github.com/purescript/purescript-node-child-process/releases)
[![Build status](https://github.com/purescript-node/purescript-node-child-process/workflows/CI/badge.svg?branch=master)](https://github.com/purescript-node/purescript-node-child-process/actions?query=workflow%3ACI+branch%3Amaster)
[![Pursuit](https://pursuit.purescript.org/packages/purescript-node-child-process/badge)](https://pursuit.purescript.org/packages/purescript-node-child-process)

Bindings to Node's `child_process` API.

## Installation

```
spago install node-child-process
```

## Documentation

Module documentation is [published on Pursuit](http://pursuit.purescript.org/packages/purescript-node-child-process).
