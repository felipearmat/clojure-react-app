function LogoSVG({ gradientColor1 = "#0074E4", gradientColor2 = "#00D2A3" }) {
  return (
    <svg
      width="450"
      height="100"
      xmlns="http://www.w3.org/2000/svg"
      xmlnsXlink="http://www.w3.org/1999/xlink"
      fill={gradientColor1}
    >
      <defs>
        <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" style={{ stopColor: gradientColor1 }} />
          <stop offset="100%" style={{ stopColor: gradientColor2 }} />
        </linearGradient>
      </defs>
      <text
        x="50%"
        y="70"
        fontFamily="Roboto, Arial, sans-serif"
        fontSize="40"
        fill="url(#gradient)"
        textAnchor="middle"
      >
        ArithmeticCalculatorAPI
      </text>
    </svg>
  );
}

export default LogoSVG;
