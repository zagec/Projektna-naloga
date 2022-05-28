const colors2 = require('tailwindcss/colors')

module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    colors: {
      bordeaux: '#342628',
      bordeaux2: '#725358',
      bordeaux3: '#68020F',
      bordeaux4: '#821724',
      powder: '#8FD8D2',
      warm: '#DF744A',
      lemon: '#DC8239',
      sunshine: '#F78733',
      earth: '#DAAD86',
      earth2: '#7F6B5A',
      blueish: '#659DBD',
      softBlue: '#E0EAF5',
      inherit: colors2.inherit,
      current: colors2.current,
      transparent: colors2.transparent,
      black: colors2.black,
      white: colors2.white,
      slate: colors2.slate,
      gray: colors2.gray,
      zinc: colors2.zinc,
      neutral: colors2.neutral,
      stone: colors2.stone,
      red: colors2.red,
      orange: colors2.orange,
      amber: colors2.amber,
      yellow: colors2.yellow,
      lime: colors2.lime,
      green: colors2.green,
      emerald: colors2.emerald,
      teal: colors2.teal,
      cyan: colors2.cyan,
      sky: colors2.sky,
      blue: colors2.blue,
      indigo: colors2.indigo,
      violet: colors2.violet,
      purple: colors2.purple,
      fuchsia: colors2.fuchsia,
      pink: colors2.pink,
      rose: colors2.rose,
    },
    extend: { 
      backgroundImage: {
        'split-white-black': "linear-gradient(to bottom, white 50% , #fdba74 50%);"
      },
      backgroundSize: {
        'size-200': '200% 200%',
      },
      backgroundPosition: {
          'pos-0': '0% 0%',
          'pos-100': '100% 100%',
      },
    },
  },
  plugins: [],
}
