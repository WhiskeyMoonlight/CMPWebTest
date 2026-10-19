(() => {
    const state = { frames: 0, worstGap: 0, over17: 0, over50: 0, last: 0, loafCount: 0, loafBlocking: 0, loafWorst: 0 };
    const tick = (time) => {
        if (state.last > 0) {
            const gap = time - state.last;
            state.frames++;
            state.worstGap = Math.max(state.worstGap, gap);
            if (gap > 17) state.over17++;
            if (gap > 50) state.over50++;
        }
        state.last = time;
        requestAnimationFrame(tick);
    };
    requestAnimationFrame(tick);
    if (PerformanceObserver.supportedEntryTypes.includes('long-animation-frame')) {
        new PerformanceObserver((list) => {
            for (const entry of list.getEntries()) {
                state.loafCount++;
                state.loafBlocking += entry.blockingDuration;
                state.loafWorst = Math.max(state.loafWorst, entry.duration);
            }
        }).observe({ type: 'long-animation-frame', buffered: false });
    }
    globalThis.__medexProbe = {
        reset() {
            Object.assign(state, { frames: 0, worstGap: 0, over17: 0, over50: 0, last: 0, loafCount: 0, loafBlocking: 0, loafWorst: 0 });
        },
        snapshot() {
            return { ...state };
        },
    };
})();
