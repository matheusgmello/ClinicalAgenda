/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        bg: {
          primary: '#FFFFFF',
          secondary: '#F8FAFC',
          card: '#FFFFFF',
          elevated: '#F1F5F9',
        },
        border: {
          subtle: '#E2E8F0',
          DEFAULT: '#CBD5E1',
          strong: '#94A3B8',
        },
        accent: {
          DEFAULT: '#DC2626',
          dim: '#B91C1C',
          glow: 'rgba(220,38,38,0.12)',
          soft: 'rgba(220,38,38,0.06)',
        },
        text: {
          primary: '#0F172A',
          secondary: '#475569',
          muted: '#94A3B8',
          inverse: '#FFFFFF',
        },
        status: {
          danger: '#DC2626',
          warning: '#D97706',
          info: '#2563EB',
          success: '#16A34A',
        },
      },
      fontFamily: {
        display: ['"Fraunces"', 'Georgia', 'serif'],
        body: ['"Plus Jakarta Sans"', 'system-ui', 'sans-serif'],
        mono: ['"DM Mono"', 'monospace'],
      },
      boxShadow: {
        card: '0 1px 3px rgba(0,0,0,0.06), 0 1px 2px rgba(0,0,0,0.04)',
        elevated: '0 4px 16px rgba(0,0,0,0.08), 0 1px 4px rgba(0,0,0,0.04)',
        glow: '0 0 20px rgba(220,38,38,0.15), 0 0 6px rgba(220,38,38,0.08)',
        'glow-sm': '0 0 10px rgba(220,38,38,0.12)',
        'inner-glow': 'inset 0 1px 0 rgba(255,255,255,0.8)',
      },
      backgroundImage: {
        'grid-pattern': 'linear-gradient(rgba(203,213,225,0.4) 1px, transparent 1px), linear-gradient(90deg, rgba(203,213,225,0.4) 1px, transparent 1px)',
        'accent-gradient': 'linear-gradient(135deg, #DC2626 0%, #B91C1C 100%)',
        'card-gradient': 'linear-gradient(160deg, #FFFFFF 0%, #F8FAFC 100%)',
        'hero-gradient': 'radial-gradient(ellipse at 60% 20%, rgba(220,38,38,0.04) 0%, transparent 60%), radial-gradient(ellipse at 10% 80%, rgba(37,99,235,0.03) 0%, transparent 50%)',
      },
      backgroundSize: {
        grid: '32px 32px',
      },
      animation: {
        'fade-in': 'fadeIn 0.4s ease forwards',
        'slide-up': 'slideUp 0.5s cubic-bezier(0.16, 1, 0.3, 1) forwards',
        'slide-in-right': 'slideInRight 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards',
        'pulse-glow': 'pulseGlow 2s ease-in-out infinite',
        'shimmer': 'shimmer 1.5s ease-in-out infinite',
      },
      keyframes: {
        fadeIn: {
          from: { opacity: '0' },
          to: { opacity: '1' },
        },
        slideUp: {
          from: { opacity: '0', transform: 'translateY(20px)' },
          to: { opacity: '1', transform: 'translateY(0)' },
        },
        slideInRight: {
          from: { opacity: '0', transform: 'translateX(-16px)' },
          to: { opacity: '1', transform: 'translateX(0)' },
        },
        pulseGlow: {
          '0%, 100%': { boxShadow: '0 0 10px rgba(220,38,38,0.1)' },
          '50%': { boxShadow: '0 0 20px rgba(220,38,38,0.2)' },
        },
        shimmer: {
          '0%': { backgroundPosition: '-200% center' },
          '100%': { backgroundPosition: '200% center' },
        },
      },
      transitionTimingFunction: {
        spring: 'cubic-bezier(0.16, 1, 0.3, 1)',
      },
    },
  },
  plugins: [],
};
