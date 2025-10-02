
export interface Timesheet {
  id?: number; // Soru işareti, bu alanın opsiyonel olduğunu belirtir (yeni oluştururken id olmaz)
  date: string;
  startTime: string;
  endTime: string;
  description: string;
}
