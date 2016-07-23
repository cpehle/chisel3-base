SBT ?= java -Xmx2G -Xss8M -XX:MaxPermSize=256M -jar $(base_dir)/sbt-launch.jar
SHELL := /bin/bash

base_dir = $(abspath .)

$(FIRRTL_JAR):
	$(MAKE) -C $(base_dir)/firrtl SBT="$(SBT)" root_dir=$(base_dir)/firrtl build-scala
