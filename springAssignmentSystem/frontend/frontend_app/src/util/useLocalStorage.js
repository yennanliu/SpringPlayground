import { useEffect, useState } from "react";

// https://youtu.be/aIr288-3AFE?si=eXNMQBNhouObYrP8&t=260

function useLocalState(defaultValue, key) {
  const [value, setValue] = useState(() => {
    const localStorageValue = localStorage.getItem(key);
    return localStorageValue !== null
      ? JSON.parse(localStorageValue)
      : defaultValue;
  });

  console.log(`localStorageValue ${key} is : ${value}`)

  useEffect(() => {
    localStorage.setItem(key, JSON.stringify(value));
    console.log(`update local storage ${key} to ${value}`)
  }, [key, value]);

  return [value, setValue];
}

// make this func as public
export { useLocalState };
