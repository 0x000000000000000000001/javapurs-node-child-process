# purescript-node-child-process

## JVM status

The Java adapter implements `spawn`/`spawn'` with `java.lang.ProcessBuilder`, piped
stdout/stderr, `exit`/`close`/`error` events, and the basic process properties.
A focused probe spawning `/bin/sh -c 'printf child-ok'` passes. The full Spec
integration suite is not yet within the JVM validation scope; see M32 in the
Javapurs plan.

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
