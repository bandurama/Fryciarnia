import {fireEvent, render, screen} from '@testing-library/react';
import App from './App';
import Main from "./pages/Main";
import Menu from "./pages/Menu";
import Meal from "./pages/Meal";
import Order from "./pages/Order/Order";
import History from "./pages/History";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Profile from "./pages/Profile/Profile";
import Manager from "./pages/Manager/Manager";
import Admin from "./pages/Admin/Admin";
import Kitchen from "./pages/Kitchen/Kitchen";
import PopupMessasgeBox from "./components/PopupMessasgeBox";
import {useState} from "react";
import {act} from "react-dom/test-utils";

test('testing /', () =>
{
  render(<Main />);
  expect(screen.getByText('Pobierz naszą apkę na telefon')).toBeInTheDocument();
  expect(screen.getByText('Dołącz do najlepszego teamu')).toBeInTheDocument();
  expect(screen.getByText('Znajdź miejscówkę')).toBeInTheDocument();
  expect(screen.getByText('POKAŻ WIĘCEJ')).toBeInTheDocument();
  expect(screen.getByRole('combobox')).toBeInTheDocument();
});

test('testing /menu', () =>
{
  render(<Menu />);
  expect(screen.getByText('Strony Głównej')).toBeInTheDocument();
  expect(screen.getByText('Wróć do')).toBeInTheDocument();
});

test('testing /meal', () =>
{
  render(<Meal />);
  expect(screen.getByText('Zamów online i odbierz w restauracji')).toBeInTheDocument();
  expect(screen.getByText('Składniki')).toBeInTheDocument();
  expect(screen.getByText('ALERGENY')).toBeInTheDocument();
});

test('testing /meal', () =>
{
  render(<Meal />);
  expect(screen.getByText('Zamów online i odbierz w restauracji')).toBeInTheDocument();
  expect(screen.getByText('Składniki')).toBeInTheDocument();
  expect(screen.getByText('ALERGENY')).toBeInTheDocument();
});

test('testing /order', () =>
{
  render(<Order />);
  expect(screen.getByText(/ZAPŁAĆ/i)).toBeInTheDocument();
  expect(screen.getByText('ALERGENY')).toBeInTheDocument();
});

test('testing /history', () =>
{
  render(<History />);
  expect(screen.getByText(/O Fryciarni/i)).toBeInTheDocument();
  expect(screen.getByText("Historia")).toBeInTheDocument();
  expect(screen.getByText("Nasi Kucharze")).toBeInTheDocument();
  expect(screen.getByText("Przyjazna atmosfera")).toBeInTheDocument();
  expect(screen.getByText(/ALERGENY/i)).toBeInTheDocument();
  expect(screen.getByText(/PŚK/i)).toBeInTheDocument();
});

test('testing /register', () =>
{
  render(<Login />);
  expect(screen.getByText("Zaloguj się")).toBeInTheDocument();
  expect(screen.getByText("Lub")).toBeInTheDocument();
  expect(screen.getByText(/zarejestruj się/i)).toBeInTheDocument();
});

test('testing /profile', () =>
{
  render(<Profile />);
  expect(screen.getAllByText("Mój Profil").length).toBe(2);
  expect(screen.getByText("Ustawienia")).toBeInTheDocument();
  expect(screen.getByText(/Zapisz/i)).toBeInTheDocument();
});

test('testing /manager', () =>
{
  render(<Manager />);
  expect(screen.getByText(/FRYCIARNIA - PANEL MENADŻERA/i)).toBeInTheDocument();
  expect(screen.getByText(/Statystyka/i)).toBeInTheDocument();
	expect(screen.getAllByText(/Zamówienia/i).length).toBeGreaterThan(1);
	expect(screen.getByText(/Obsługa/i)).toBeInTheDocument();
});

test('testing /admin', () =>
{
  render(<Admin />);
  expect(screen.getByText(/PANEL ADMINISTRACYJNY/i)).toBeInTheDocument();
  expect(screen.getAllByText(/Franczyzy/i).length).toBe(2);
  expect(screen.getAllByText(/Posiłki/i).length).toBeLessThan(10);
  expect(screen.getAllByText(/Składniki/i).length).toBeCloseTo(2, 4);
  expect(screen.getAllByText(/Zamówienia/i).length).toBeGreaterThanOrEqual(2);
  expect(screen.getByText(/Użytkownicy/i)).toBeInTheDocument();
});

test('testing /kitchen', () =>
{
  render(<Kitchen />);
  expect(screen.getByText(/Wyloguj/i)).toBeInTheDocument();
  expect(screen.getByAltText('logout')).toBeInTheDocument();
});
test('testing component PopupMessageBox', () =>
{
  const onOk = jest.fn();
  const onCancel = jest.fn();
  const isShown = true;

  render(<PopupMessasgeBox onOk={onOk} onCancel={onCancel} setShown={() => {}} isShown={isShown} messesage="test" />);
  const ok = screen.getByText('👍');
  const fail = screen.getByText('👎');
  expect(ok).toBeInTheDocument();
  expect(fail).toBeInTheDocument();
  fireEvent.click(ok);
  expect(onOk).toHaveBeenCalled();
  fireEvent.click(fail);
  expect(onCancel).toHaveBeenCalled();
});
