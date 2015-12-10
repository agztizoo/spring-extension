# spring-extension
1.在模块化中，id相同的bean定义，根据override-order从小到大依次覆盖

#spring-pipeline
1. 扩展spring配置，实现一个简单的pipeline功能，用法如下：
<pipeline:chain id="chanId" header="firstPipelineHandlerId">
    <pipeline:handler id="firstPipelineHandlerId" bean="Spring handler bean">
        <pipeline:next returnvalue="1" handler="NexthandlerId"/>
    </pipeline:handler>
    <pipeline:handler id="NexthandlerId" bean="Spring handler bean"/>
</pipeline:chain>
