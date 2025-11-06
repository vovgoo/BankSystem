import { createSystem, defaultConfig, defineConfig } from '@chakra-ui/react';

const customConfig = defineConfig({
  theme: {
    tokens: {
      colors: {
        primary: {
          50: { value: '#E0F7FA' },
          100: { value: '#B2EBF2' },
          200: { value: '#80DEEA' },
          300: { value: '#4DD0E1' },
          400: { value: '#26C6DA' },
          500: { value: '#00B1B1' },
          600: { value: '#008B8B' },
          700: { value: '#006B6B' },
          800: { value: '#004D4D' },
          900: { value: '#0B262B' },
        },
      },
    },
  },
});

export const theme = createSystem(defaultConfig, customConfig);

export const colors = {
  background: {
    canvas: '#040404',
    card: '#1A1A1A',
    border: '#2A2A2A',
    table: '#1A1A1A',
  },
  text: {
    primary: '#E0E0E0',
    secondary: '#B0B0B0',
    muted: '#808080',
  },
  primary: '#FFFFFF',
} as const;
