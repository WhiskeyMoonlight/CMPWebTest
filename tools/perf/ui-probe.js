(() => {
    const frames = { intervals: [], last: 0, loafCount: 0, loafBlocking: 0, loafWorst: 0 };
    const input = { pointerdown: 0, pointercancel: 0, touchmove: 0, touchmovePrevented: 0 };
    const tick = (time) => {
        if (frames.last > 0) frames.intervals.push(time - frames.last);
        frames.last = time;
        requestAnimationFrame(tick);
    };
    requestAnimationFrame(tick);
    if (PerformanceObserver.supportedEntryTypes.includes('long-animation-frame')) {
        new PerformanceObserver((list) => {
            for (const entry of list.getEntries()) {
                frames.loafCount++;
                frames.loafBlocking += entry.blockingDuration;
                frames.loafWorst = Math.max(frames.loafWorst, entry.duration);
            }
        }).observe({ type: 'long-animation-frame', buffered: false });
    }
    addEventListener('pointerdown', () => input.pointerdown++, true);
    addEventListener('pointercancel', () => input.pointercancel++, true);
    addEventListener('touchmove', (event) => {
        input.touchmove++;
        if (event.defaultPrevented) input.touchmovePrevented++;
    }, { passive: true });

    const a11yRoot = () => {
        for (const element of document.body.querySelectorAll('*')) {
            const root = element.shadowRoot?.getElementById('cmp_a11y_root');
            if (root) return root;
        }
        return null;
    };
    const findNode = (predicate) => {
        const root = a11yRoot();
        if (!root) return null;
        for (const element of root.querySelectorAll('*')) {
            if (predicate(element)) {
                const rect = element.getBoundingClientRect();
                return { x: rect.left + rect.width / 2, y: rect.top + rect.height / 2, left: rect.left, top: rect.top, width: rect.width, height: rect.height };
            }
        }
        return null;
    };

    globalThis.__medexUiProbe = {
        reset() {
            Object.assign(frames, { intervals: [], last: 0, loafCount: 0, loafBlocking: 0, loafWorst: 0 });
            Object.assign(input, { pointerdown: 0, pointercancel: 0, touchmove: 0, touchmovePrevented: 0 });
        },
        snapshot() {
            return { frames: { ...frames, intervals: [...frames.intervals] }, input: { ...input } };
        },
        text() {
            return a11yRoot()?.innerText ?? '';
        },
        byText(text) {
            return findNode((element) => [...element.childNodes]
                .some((node) => node.nodeType === Node.TEXT_NODE && node.textContent.trim() === text));
        },
        byLabel(label) {
            return findNode((element) => element.getAttribute('aria-label') === label);
        },
    };
})();
