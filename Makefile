DIR_BUILD = build

SBT ?= sbt
_SBT_FLAGS? ?= -Dsbt.log.noformat=true
SBT_FLAGS ?=
SHELL := /bin/bash

base_dir = $(abspath .)
