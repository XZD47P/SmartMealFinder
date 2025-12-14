import {Calendar, ChevronLeft, ChevronRight} from "lucide-react";

const WeekNavigator = ({currentYear, currentWeek, onPrev, onNext, onToday}) => {
    return (
        <div
            className="flex items-center justify-between bg-white p-3 rounded-xl shadow-sm border border-gray-200 mb-4 mx-4 md:mx-0">

            <button
                onClick={onPrev}
                className="p-2 hover:bg-gray-100 rounded-full text-gray-600 transition-colors"
            >
                <ChevronLeft className="w-5 h-5"/>
            </button>

            <div className="flex flex-col items-center">
                <div className="flex items-center gap-2 text-sm font-semibold text-blue-600 uppercase tracking-wider">
                    <Calendar className="w-3 h-3"/>
                    <span>Year {currentYear}</span>
                </div>
                <h2 className="text-lg font-bold text-gray-800 leading-tight">
                    Week {currentWeek}
                </h2>
            </div>

            <div className="flex items-center gap-2">
                <button
                    onClick={onToday}
                    className="hidden sm:block text-xs font-bold text-blue-600 hover:bg-blue-50 px-3 py-1.5 rounded-md transition-colors"
                >
                    Today
                </button>
                <button
                    onClick={onNext}
                    className="p-2 hover:bg-gray-100 rounded-full text-gray-600 transition-colors"
                >
                    <ChevronRight className="w-5 h-5"/>
                </button>
            </div>
        </div>
    );
};

export default WeekNavigator;