/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        bg: {
          primary: '#06080F',
          secondary: '#0D1220',
          card: '#111827',
          elevated: '#161F30',
        },
        border: {
          subtle: '#1A2540',
          DEFAULT: '#1E2D45',
          strong: '#2A3E5E',
        },
        accent: {
          DEFAULT: '#00C896',
          dim: '#00916E',
          glow: 'rgba(0,200,150,0.15)',
          soft: 'rgba(0,200,150,0.08)',
        },
        text: {
          primary: '#EDF0F7',
          secondary: '#7B8BA8',
          muted: '#3D4F6B',
          inverse: '#06080F',
        },
        status: {
          danger: '#FF4D6D',
          warning: '#FFBA08',
          info: '#3B82F6',
          success: '#00C896',
        },
      },
      fontFamily: {
        display: ['"Fraunces"', 'Georgia', 'serif'],
        body: ['"Plus Jakarta Sans"', 'system-ui', 'sans-serif'],
        mono: ['"DM Mono"', 'monospace'],
      },
      boxShadow: {
        card: '0 1px 3px rgba(0,0,0,0.4), 0 4px 16px rgba(0,0,0,0.3)',
        elevated: '0 4px 24px rgba(0,0,0,0.5), 0 1px 4px rgba(0,0,0,0.4)',
        glow: '0 0 24px rgba(0,200,150,0.2), 0 0 8px rgba(0,200,150,0.1)',
        'glow-sm': '0 0 12px rgba(0,200,150,0.15)',
        'inner-glow': 'inset 0 1px 0 rgba(255,255,255,0.05)',
      },
      backgroundImage: {
        'grid-pattern': 'linear-gradient(rgba(30,45,69,0.3) 1px, transparent 1px), linear-gradient(90deg, rgba(30,45,69,0.3) 1px, transparent 1px)',
        'accent-gradient': 'linear-gradient(135deg, #00C896 0%, #00916E 100%)',
        'card-gradient': 'linear-gradient(160deg, #111827 0%, #0D1220 100%)',
        'hero-gradient': 'radial-gradient(ellipse at 60% 40%, rgba(0,200,150,0.06) 0%, transparent 60%), radial-gradient(ellipse at 20% 80%, rgba(59,130,246,0.04) 0%, transparent 50%)',
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
          '0%, 100%': { boxShadow: '0 0 12px rgba(0,200,150,0.15)' },
          '50%': { boxShadow: '0 0 24px rgba(0,200,150,0.3)' },
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
