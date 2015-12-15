# spring-extension
1.在模块化中，id相同的bean定义，根据override-order从小到大依次覆盖

#spring-pipeline模块
1. 扩展spring配置，实现一个简单的pipeline功能，用法如下：

\<pipeline:chain id="chanId" header="firstPipelineHandlerId"><br/>
   &nbsp;&nbsp;&nbsp;&nbsp;\<pipeline:handler id="firstPipelineHandlerId" bean="Spring handler bean"><br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\<pipeline:next returnvalue="1" handler="NexthandlerId"/><br/>
    &nbsp;&nbsp;&nbsp;&nbsp;\</pipeline:handler><br/>
    &nbsp;&nbsp;&nbsp;&nbsp;\<pipeline:handler id="NexthandlerId" bean="Spring handler bean"/><br/>
\</pipeline:chain>
