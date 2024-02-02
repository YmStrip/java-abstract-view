import yview.src.Compiler;

public class Test {
	@org.junit.jupiter.api.Test
	void test() {
		var code = """
			<template>
					<div v-for="(d) in b" v-if="a">this is a div</div>
					<div v-else-if="4"></div>
					<div v-else-if="4"></div>
				</template>
				<script>
						var a = ref(1)
						var b = prop('name')
				</script>
			""";
		var compiler = new Compiler();
		compiler.compile(code);
	}
}
