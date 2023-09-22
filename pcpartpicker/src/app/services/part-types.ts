export class Cpu {
    partName: string = '';
    manufacturerName: string = '';
    cpuCores: number = 0;
    cpuThreads: number = 0;
    maxTurboFrequencyInGHz: number = 0;
    defaultFrequencyInGHz: number = 0;
    TDPinWattage: number = 0;
    chipGeneration: number = 0;
    supportedRAMType: string = '';
    socket: string = '';
}

export class Gpu {
    partName: string = '';
    manufacturerName: string = '';
    clockspeedInMHz: number = 0;
    gpuRAMtype: string = '';
    gpuRAMinGB: number = 0;
    wattage: number = 0;
    chipsetManufacturer: string = '';
    gpuInterface: string = '';
    amountCUDACores: number = 0;
}

export class Mainboard {
    partName: string = '';
    manufacturerName:string = '';
    sizeFactor: string = '';
    cpuManufacturer: string = '';
    cpuSocket: string = '';
    supportedRAMType: string = '';
    chipGeneration: string = '';
    storageI: string = '';
    hasIntegratedW: string = '';
}

export class PSU {
    partName:  string = '';
    manufacturerName: string = '';
    powerInWattage: string = '';
    efficiencyInPercent: string = '';
    certificate: string = '';
    connections: string = '';
    sizeFactor: string = '';
    isModular: string = '';
}

export class Case {
    partName: string = "";
    manufacturerName: string = "Nvidia";
    internalFanAmount: number = 3;
    frameColor: string = '';
    sizeFactor: string = '';
    towerType: string = '';
    hasRGB: boolean = false;
    hasSeeThroughSidepanel: boolean = false;
}

export class Fan {
    partName: string = '';
    manufacturerName: string = '';
    supportedSockets: string = '';
    fanType: string = '';
    connections: string = '';
    fanSpeedInUmin: number = 0;
    volumeIndB: number = 0;
    maxCoolingInWattage: number = 0;
}

export interface Pair {
    key?: any;
    value?: any;
}
