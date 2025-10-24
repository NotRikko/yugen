import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Button } from "@/shared/ui/button";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormMessage,
} from "@/shared/ui/form";
import { Input } from "@/shared/ui/input";
import { Textarea } from "@/shared/ui/textarea";

import { useUser } from "@/features/user/useUserContext";
import { useState } from "react";
import { productApi } from "../api/productApi";

const formSchema = z.object({
  name: z.string().min(1, "Product name is required"),
  description: z.string().min(1, "Description is required"),
  price: z.number().positive("Price must be positive"),
  quantityInStock: z.number().int().nonnegative("Quantity must be 0 or more"),
  seriesIds: z.array(z.number()).optional(),
  collectionIds: z.array(z.number()).optional(),
  files: z.array(z.instanceof(File)).optional(),
});

export default function ProductCreate() {
  const { user } = useUser();
  const [previewImages, setPreviewImages] = useState<string[]>([]);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      description: "",
      price: 0,
      quantityInStock: 0,
      seriesIds: [],
      collectionIds: [],
      files: [],
    },
  });

  if (!user.artistId) {
    return (
      <div className="w-5/6 mx-auto p-4 border">
        <p className="text-red-500">
          Only artists can create products. Please register as an artist to create.
        </p>
      </div>
    );
  }

  async function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      await productApi.createProduct({
        artistId: user.artistId,
        name: values.name,
        description: values.description,
        price: values.price,
        quantityInStock: values.quantityInStock,
        seriesIds: values.seriesIds ?? [],
        collectionIds: values.collectionIds ?? [],
        files: values.files,
      });
      console.log("Product created successfully");
      form.reset();
      setPreviewImages([]);
    } catch (error) {
      console.error("Error creating product", error);
    }
  }

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="space-y-6 py-8 w-5/6 mx-auto p-4 border rounded-lg shadow-md bg-white"
      >
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Input placeholder="Product Name" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="description"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Textarea placeholder="Product Description" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="price"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Input
                  type="number"
                  placeholder="Price"
                  {...field}
                  value={field.value || ""}
                  onChange={(e) => field.onChange(Number(e.target.value))}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="quantityInStock"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Input
                  type="number"
                  placeholder="Quantity in Stock"
                  {...field}
                  value={field.value || ""}
                  onChange={(e) => field.onChange(Number(e.target.value))}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="files"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Input
                  type="file"
                  accept="image/*"
                  multiple
                  onChange={(e) => {
                    const selectedFiles = e.target.files
                      ? Array.from(e.target.files)
                      : [];
                    const updatedFiles = [...(field.value || []), ...selectedFiles];
                    if (updatedFiles.length > 4) {
                      alert("You can upload a maximum of 4 images.");
                      return;
                    }
                    field.onChange(updatedFiles);
                    setPreviewImages(updatedFiles.map((file) => URL.createObjectURL(file)));
                  }}
                />
              </FormControl>
              <FormDescription>Upload up to 4 images for your product.</FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {previewImages.map((src, index) => (
            <div key={index} className="relative w-full h-32 md:h-40 lg:h-48">
              <img
                src={src}
                alt={`Preview ${index + 1}`}
                className="rounded-md border w-full h-full object-cover"
              />
            </div>
          ))}
        </div>

        <Button type="submit">Create Product</Button>
      </form>
    </Form>
  );
}