package juuxel.adorn.computer.clay

data class ClayFile(val functions: List<Function>) {
    data class Function(val name: String, val parameters: List<String>, val instructions: List<Instruction>)
}
