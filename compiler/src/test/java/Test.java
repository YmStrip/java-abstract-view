import yview.src.Compiler;

public class Test {
	@org.junit.jupiter.api.Test
	void test() {
		var code = """
			<template>
				<div v-if="abc"/>
				<div v-else-if="ac"/>
				<div v-else="ac1"/>
				<div v-if="abc1"/>
				<div v-else v-for="(d,i) in b">
					<div v-if="i<5">this is a div</div>
					<div v-else>this is a div{{d}}</div>
				</div>
			</template>
			<script>
				var a = ref(1)
				var b = prop('name')
			</script>
			""";
		var compiler = new Compiler();
		compiler.compileSfc(code);
		var js = compiler.sfcAst.toString();
		System.out.println(js);
	}
}
