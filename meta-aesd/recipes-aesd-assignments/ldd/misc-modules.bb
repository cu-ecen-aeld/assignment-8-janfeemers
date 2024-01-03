LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

PV = "1.0+git${SRCPV}"
SRCREV = "d4d13551142bf2f4abab196657d291958344c66e"
SRC_URI = "git://github.com/cu-ecen-aeld/assignment-7-janfeemers.git;protocol=https;branch=master \
           file://fix_build.patch \
           file://misc-modules-start-stop \
           "

S = "${WORKDIR}/git"

inherit module
inherit update-rc.d

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/misc-modules"
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/scull"

do_install () {
	install -d ${D}/lib/modules/${KERNEL_VERSION}
	install -m 0755 ${S}/scull/scull.ko ${D}/${base_libdir}/modules/${KERNEL_VERSION}/
	install -m 0755 ${S}/misc-modules/hello.ko ${D}/${base_libdir}/modules/${KERNEL_VERSION}
	install -m 0755 ${S}/misc-modules/faulty.ko ${D}/${base_libdir}/modules/${KERNEL_VERSION}

	install -d ${D}/${bindir}/
	install -m 0755 ${S}/scull/scull_load ${D}/${bindir}/
	install -m 0755 ${S}/scull/scull_unload ${D}/${bindir}/
	install -m 0755 ${S}/misc-modules/module_load ${D}/${bindir}/
	install -m 0755 ${S}/misc-modules/module_unload ${D}/${bindir}/

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/misc-modules-start-stop ${D}${sysconfdir}/init.d	
}

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "misc-modules-start-stop"


#KERNEL_MODULE_AUTOLOAD += "scull"

FILES:${PN} += "${sysconfdir}/init.d/scull-start-stop"
FILES:${PN} += "${sysconfdir}/init.d/misc-modules-start-stop"
FILES:${PN} += "${bindir}/*"
