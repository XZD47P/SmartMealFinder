export const getISOWeekInfo = (date = new Date()) => {
    const target = new Date(date.valueOf());
    const dayNumber = (date.getDay() + 6) % 7; // Adjust 0 (Sunday) to 6

    // Set to nearest Thursday: current date + 4 - current day number
    // ISO 8601 definitions rely on Thursday to determine the week year
    target.setDate(target.getDate() - dayNumber + 3);

    const firstThursday = target.valueOf();
    target.setMonth(0, 1);

    if (target.getDay() !== 4) {
        target.setMonth(0, 1 + ((4 - target.getDay()) + 7) % 7);
    }

    const weekNumber = 1 + Math.ceil((firstThursday - target) / 604800000);
    const year = target.getFullYear(); // This is the Week-Based Year

    return {year, weekNumber};
};