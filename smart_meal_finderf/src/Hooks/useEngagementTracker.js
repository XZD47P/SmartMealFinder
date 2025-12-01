import {useEffect, useRef} from "react";
import api from "../Backend/api";

const useEngagementTracker = (recipe, user) => {
    const seenEventFired = useRef(false);
    const readEventFired = useRef(false);

    useEffect(() => {
        if (!user || !recipe) {
            return;
        }
        //Reset
        seenEventFired.current = false;
        readEventFired.current = false;

        const SEEN_THRESHOLD = 1000; //1s
        const READ_THRESHOLD = 10000; //10s

        const seenTimer = setTimeout(() => {
            if (!seenEventFired.current) {
                api.post("/recipe/seen", recipe)
                seenEventFired.current = true;
            }
        }, SEEN_THRESHOLD);

        const readTimer = setTimeout(() => {
            if (!readEventFired.current) {
                api.post("/recipe/read", recipe)
                readEventFired.current = true;
            }
        }, READ_THRESHOLD);

        return () => {
            clearTimeout(seenTimer);
            clearTimeout(readTimer);
        }
    }, [recipe, user]);
}

export default useEngagementTracker;