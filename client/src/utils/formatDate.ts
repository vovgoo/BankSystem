export const formatDate = (timestamp: string) => {
  const date = new Date(timestamp);
  const offsetMinutes = -date.getTimezoneOffset();
  const localDate = new Date(date.getTime() + offsetMinutes * 60 * 1000);

  return localDate.toLocaleString("ru-RU", {
    day: "numeric",
    month: "long",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};