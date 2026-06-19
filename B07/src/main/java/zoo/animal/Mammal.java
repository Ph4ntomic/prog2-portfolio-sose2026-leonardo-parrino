package zoo.animal;

public sealed interface Mammal extends Animal permits Primate, Rodent, Cat, Elephant, Dolphin {}
