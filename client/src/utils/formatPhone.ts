export function formatPhone(phone: string): string {
  const digits = phone.replace(/[^\d]/g, '');

  if (!digits.startsWith('375')) return phone;

  const code = digits.slice(0, 3);
  const operator = digits.slice(3, 5);
  const part1 = digits.slice(5, 8);
  const part2 = digits.slice(8, 10);
  const part3 = digits.slice(10, 12);

  return `+${code} (${operator}) ${part1}-${part2}-${part3}`;
}
