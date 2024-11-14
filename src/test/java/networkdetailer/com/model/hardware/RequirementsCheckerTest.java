package networkdetailer.com.model.hardware;

import networkdetailer.com.model.data.CPUData;
import networkdetailer.com.model.data.CPUGeneration;
import networkdetailer.com.model.data.CPUManufacturer;
import networkdetailer.com.model.data.MemoryData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequirementsCheckerTest {
    private RequirementsChecker requirementsChecker;

    @BeforeEach
    void setUp() {
        requirementsChecker = new RequirementsChecker();
    }

    private static Stream<Arguments> computerParametersIntel() {
        return Stream.of(
                Arguments.of(8, 12, 122, true),
                Arguments.of(7, 4, 122, false),
                Arguments.of(7, 16, 122, false),
                Arguments.of(7, 32, 512, false),
                Arguments.of(8, 32, 122, true),
                Arguments.of(14, 22, 1212, true),
                Arguments.of(8, 22, 1212, true)
        );
    }

    @ParameterizedTest
    @MethodSource("computerParametersIntel")
    void parametrizedIntelTest(int generation, int ramSpace, int diskSpace, boolean shouldPass) {
        CPUData cpuData = mock(CPUData.class);
        CPUGeneration cpuGeneration = mock(CPUGeneration.class);
        MemoryData memoryData = mock(MemoryData.class);

        when(cpuData.generation()).thenReturn(cpuGeneration);
        when(cpuGeneration.cpuManufacturer()).thenReturn(CPUManufacturer.INTEL);
        when(cpuGeneration.generation()).thenReturn(generation);

        when(memoryData.ramGB()).thenReturn(ramSpace);
        when(memoryData.diskSpaceGB()).thenReturn(diskSpace);

        boolean result = requirementsChecker.check(cpuData, memoryData);
        assertEquals(shouldPass, result);
    }

    private static Stream<Arguments> computerParametersAmd() {
        return Stream.of(
                Arguments.of(8, 12, 122, true),
                Arguments.of(7, 4, 122, true),
                Arguments.of(8, 32, 122, true),
                Arguments.of(14, 22, 1212, true),
                Arguments.of(8, 22, 1212, true),
                Arguments.of(1, 22, 1212, false),
                Arguments.of(0, 22, 1212, false)
        );
    }

    @ParameterizedTest
    @MethodSource("computerParametersAmd")
    void parametrizedAmdTest(int generation, int ramSpace, int diskSpace, boolean shouldPass) {
        CPUData cpuData = mock(CPUData.class);
        CPUGeneration cpuGeneration = mock(CPUGeneration.class);
        MemoryData memoryData = mock(MemoryData.class);

        when(cpuData.generation()).thenReturn(cpuGeneration);
        when(cpuGeneration.cpuManufacturer()).thenReturn(CPUManufacturer.AMD);
        when(cpuGeneration.generation()).thenReturn(generation);

        when(memoryData.ramGB()).thenReturn(ramSpace);
        when(memoryData.diskSpaceGB()).thenReturn(diskSpace);

        boolean result = requirementsChecker.check(cpuData, memoryData);
        assertEquals(shouldPass, result);
    }

    private static Stream<Arguments> computerParametersApple() {
        return Stream.of(
                Arguments.of(1, 12, 122, false),
                Arguments.of(2, 22, 122, false),
                Arguments.of(3, 32, 256, false)
        );
    }

    @ParameterizedTest
    @MethodSource("computerParametersApple")
    void parametrizedAppleTest(int generation, int ramSpace, int diskSpace, boolean shouldPass) {
        CPUData cpuData = mock(CPUData.class);
        CPUGeneration cpuGeneration = mock(CPUGeneration.class);
        MemoryData memoryData = mock(MemoryData.class);

        when(cpuData.generation()).thenReturn(cpuGeneration);
        when(cpuGeneration.cpuManufacturer()).thenReturn(CPUManufacturer.APPLE);
        when(cpuGeneration.generation()).thenReturn(generation);

        when(memoryData.ramGB()).thenReturn(ramSpace);
        when(memoryData.diskSpaceGB()).thenReturn(diskSpace);

        boolean result = requirementsChecker.check(cpuData, memoryData);
        assertEquals(shouldPass, result);
    }
}