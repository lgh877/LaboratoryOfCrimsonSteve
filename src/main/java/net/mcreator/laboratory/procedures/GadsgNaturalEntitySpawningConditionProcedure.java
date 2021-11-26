package net.mcreator.laboratory.procedures;

import java.util.Map;

public class GadsgNaturalEntitySpawningConditionProcedure {
	public static boolean executeProcedure(Map<String, Object> dependencies) {
		return (Math.random() < 0.1);
	}
}
