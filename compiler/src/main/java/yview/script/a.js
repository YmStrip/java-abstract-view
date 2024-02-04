//看上去好像也没什么问题
//TODO 参数变量之类的处理 , 简单处理方案
//1. 使用脏检测
var a = {
    a: 56,
    render() {
        return h("template", [
            abc ? h("div") : (
                ac ? h("div") : h("div")
            ),
            abc1 ? h("div") : this.b.map((d, i) => h("div", [
                i < 5 ? h("div") : h("div")
            ]))
        ])
    }
}