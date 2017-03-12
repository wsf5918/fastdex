package com.dx168.fastdex.build.task

import com.dx168.fastdex.build.util.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.tencent.tinker.build.aapt.AaptResourceCollector
import com.tencent.tinker.build.aapt.AaptUtil
import com.tencent.tinker.build.aapt.PatchUtil
import com.tencent.tinker.build.aapt.RDotTxtEntry

/**
 * Created by tong on 17/3/11.
 */
public class FastdexResourceIdTask extends DefaultTask {
    static final String RESOURCE_PUBLIC_XML = "public.xml"
    static final String RESOURCE_IDX_XML = "idx.xml"

    String resDir
    String variantName

    FastdexResourceIdTask() {
        group = 'fastdex'
    }

    @TaskAction
    def applyResourceId() {
        File buildDir = FileUtils.getFastdexBuildDir(project,variantName)
        String resourceMappingFile = new File(buildDir,FileUtils.R_TXT)

        // Parse the public.xml and ids.xml
        if (!FileUtils.isLegalFile(resourceMappingFile)) {
            project.logger.error("apply resource mapping file ${resourceMappingFile} is illegal, just ignore")
            return
        }
        String idsXml = resDir + "/values/ids.xml";
        String publicXml = resDir + "/values/public.xml";
        FileUtils.deleteFile(idsXml);
        FileUtils.deleteFile(publicXml);
        List<String> resourceDirectoryList = new ArrayList<String>()
        resourceDirectoryList.add(resDir)

        project.logger.error("we build ${project.getName()} apk with apply resource mapping file ${resourceMappingFile}")
        Map<RDotTxtEntry.RType, Set<RDotTxtEntry>> rTypeResourceMap = PatchUtil.readRTxt(resourceMappingFile)

        AaptResourceCollector aaptResourceCollector = AaptUtil.collectResource(resourceDirectoryList, rTypeResourceMap)
        PatchUtil.generatePublicResourceXml(aaptResourceCollector, idsXml, publicXml)
        File publicFile = new File(publicXml)


        if (publicFile.exists()) {
            FileUtils.copyFileUsingStream(publicFile, new File(buildDir,RESOURCE_PUBLIC_XML))
            project.logger.error("==fastdex gen resource public.xml in ${RESOURCE_PUBLIC_XML}")
        }
        File idxFile = new File(idsXml)
        if (idxFile.exists()) {
            FileUtils.copyFileUsingStream(idxFile, new File(buildDir,RESOURCE_IDX_XML))
            project.logger.error("==fastdex gen resource idx.xml in ${RESOURCE_IDX_XML}")
        }
    }
}

