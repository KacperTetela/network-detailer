package networkdetailer.com.model.hardware;

import networkdetailer.com.model.data.CPUGeneration;
import networkdetailer.com.model.data.CPUManufacturer;

public class CPUGenerationGetter {

    public CPUGeneration identify(String cpuModel) {
        CPUManufacturer manufacturer = CPUManufacturer.UNKNOWN;
        int generation = -1;

        if (cpuModel.contains("Intel")) {
            manufacturer = CPUManufacturer.INTEL;
        } else if (cpuModel.contains("AMD")) {
            manufacturer = CPUManufacturer.AMD;
        } else if (cpuModel.contains("Apple")) {
            manufacturer = CPUManufacturer.APPLE;
        }

        if (manufacturer != CPUManufacturer.UNKNOWN) {
            String[] parts = cpuModel.split("-");

            if (manufacturer == CPUManufacturer.APPLE) {
                String modelNumber = parts[0].replaceAll("[^0-9]", "");
                if (!modelNumber.isEmpty()) {
                    generation = Integer.parseInt(modelNumber);
                }
            } else if (parts.length > 1) {
                String modelNumber = parts[1];
                char firstChar = modelNumber.charAt(0);

                if (Character.isDigit(firstChar)) {
                    generation = Character.getNumericValue(firstChar);

                    if (manufacturer == CPUManufacturer.INTEL && generation == 1 && modelNumber.length() >= 5) {
                        char secondChar = modelNumber.charAt(1);
                        if (Character.isDigit(secondChar)) {
                            generation = 10 + Character.getNumericValue(secondChar);
                        }
                    }
                }
            }
        }

        return new CPUGeneration(manufacturer, generation);
    }
}
